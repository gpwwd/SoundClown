package com.soundclown.track.application.service;

import com.soundclown.track.application.dto.request.genre.CreateGenreRequest;
import com.soundclown.track.application.dto.request.genre.UpdateGenreRequest;
import com.soundclown.track.application.dto.response.genre.GenreResponse;
import com.soundclown.track.application.repository.GenreRepository;
import com.soundclown.track.application.usecase.genre.GenreUseCase;
import com.soundclown.track.domain.model.Genre;
import com.soundclown.track.domain.valueobject.Name;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService implements GenreUseCase {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public GenreResponse createGenre(CreateGenreRequest request) {
        Name name = new Name(request.name());
        
        // Проверка на дублирование имени
        if (genreRepository.existsByName(name)) {
            throw new IllegalArgumentException("Genre with name '" + name.getValue() + "' already exists");
        }
        
        Genre genre = Genre.create(name);
        Genre saved = genreRepository.save(genre);
        
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public GenreResponse updateGenre(Long id, UpdateGenreRequest request) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id));
        
        Name name = new Name(request.name());
        
        // Проверка на дублирование имени
        if (!genre.getName().getValue().equals(name.getValue()) && 
                genreRepository.existsByName(name)) {
            throw new IllegalArgumentException("Genre with name '" + name.getValue() + "' already exists");
        }
        
        genre.updateName(name);
        Genre saved = genreRepository.save(genre);
        
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GenreResponse getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id));
        
        return mapToResponse(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreResponse> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id));
        
        genreRepository.delete(genre);
    }
    
    private GenreResponse mapToResponse(Genre genre) {
        return new GenreResponse(
                genre.getId(),
                genre.getName().getValue()
        );
    }
} 