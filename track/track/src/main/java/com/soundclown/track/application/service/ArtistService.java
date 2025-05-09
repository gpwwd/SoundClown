package com.soundclown.track.application.service;

import com.soundclown.track.application.dto.request.artist.CreateArtistRequest;
import com.soundclown.track.application.dto.request.artist.UpdateArtistRequest;
import com.soundclown.track.application.dto.response.ArtistResponse;
import com.soundclown.track.application.mapper.ArtistMapper;
import com.soundclown.track.application.repository.ArtistRepository;
import com.soundclown.track.application.repository.GenreRepository;
import com.soundclown.track.application.usecase.ArtistUseCase;
import com.soundclown.track.domain.model.Artist;
import com.soundclown.track.domain.model.Genre;
import com.soundclown.track.domain.service.ArtistNameUniquenessChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistService implements ArtistUseCase {

    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final ArtistNameUniquenessChecker uniquenessChecker;
    private final ArtistMapper artistMapper;

    @Override
    @Transactional
    public ArtistResponse createArtist(CreateArtistRequest request, Long userId) {
        if (artistRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("User already has an artist profile");
        }

        Artist artist = Artist.create(userId, request.name(), request.description(), uniquenessChecker);
        Artist saved = artistRepository.save(artist);
        
        return artistMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ArtistResponse updateArtist(Long userId, UpdateArtistRequest request) {
        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found for user with id: " + userId));
        
        artist.update(request.name(), request.description(), uniquenessChecker);
        Artist saved = artistRepository.save(artist);
        
        return artistMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ArtistResponse getArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + id));
        
        return artistMapper.toResponse(artist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtistResponse> getAllArtists() {
        return artistRepository.findAll().stream()
                .map(artistMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + id));
        
        artistRepository.delete(artist);
    }

    @Override
    @Transactional
    public void addGenreToArtist(Long artistId, Long genreId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistId));
        
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
        
        artist.addGenre(genre);
        artistRepository.save(artist);
    }

    @Override
    @Transactional
    public void removeGenreFromArtist(Long artistId, Long genreId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistId));
        
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
        
        artist.removeGenre(genre);
        artistRepository.save(artist);
    }
} 