package com.soundclown.track.application.service;

import com.soundclown.track.application.dto.request.genre.CreateGenreRequest;
import com.soundclown.track.application.dto.request.genre.UpdateGenreRequest;
import com.soundclown.track.application.dto.response.GenreResponse;
import com.soundclown.track.application.mapper.GenreMapper;
import com.soundclown.track.application.repository.GenreRepository;
import com.soundclown.track.application.usecase.GenreUseCase;
import com.soundclown.track.domain.model.Genre;
import com.soundclown.track.domain.service.GenreNameUniquenessChecker;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService implements GenreUseCase {

    private final GenreRepository genreRepository;
    private final GenreNameUniquenessChecker uniquenessChecker;
    private final GenreMapper genreMapper;

    public GenreService(
            GenreRepository genreRepository, 
            GenreNameUniquenessChecker uniquenessChecker,
            GenreMapper genreMapper
    ) {
        this.genreRepository = genreRepository;
        this.uniquenessChecker = uniquenessChecker;
        this.genreMapper = genreMapper;
    }

    @Override
    @Transactional
    public GenreResponse createGenre(CreateGenreRequest request) {
        Genre genre = Genre.create(request.name(), uniquenessChecker);
        Genre saved = genreRepository.save(genre);
        
        return genreMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public GenreResponse updateGenre(Long id, UpdateGenreRequest request) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id));
        
        genre.updateName(request.name(), uniquenessChecker);
        Genre saved = genreRepository.save(genre);
        
        return genreMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GenreResponse getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id));
        
        return genreMapper.toResponse(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreResponse> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id));
        
        genreRepository.delete(genre);
    }
} 