package com.soundclown.track.application.service;

import com.soundclown.track.application.dto.request.artist.CreateArtistRequest;
import com.soundclown.track.application.dto.request.artist.UpdateArtistRequest;
import com.soundclown.track.application.dto.response.artist.ArtistResponse;
import com.soundclown.track.application.dto.response.genre.GenreResponse;
import com.soundclown.track.application.repository.ArtistRepository;
import com.soundclown.track.application.repository.GenreRepository;
import com.soundclown.track.application.usecase.artist.ArtistUseCase;
import com.soundclown.track.domain.model.Artist;
import com.soundclown.track.domain.model.Genre;
import com.soundclown.track.domain.valueobject.Description;
import com.soundclown.track.domain.valueobject.Name;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistService implements ArtistUseCase {

    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;

    public ArtistService(ArtistRepository artistRepository, GenreRepository genreRepository) {
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public ArtistResponse createArtist(CreateArtistRequest request) {
        Name name = new Name(request.name());
        Description description = new Description(request.description());
        
        // Проверка на дублирование имени
        if (artistRepository.existsByName(name)) {
            throw new IllegalArgumentException("Artist with name '" + name.getValue() + "' already exists");
        }
        
        Artist artist = Artist.create(name, description);
        Artist saved = artistRepository.save(artist);
        
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ArtistResponse updateArtist(Long id, UpdateArtistRequest request) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + id));
        
        Name name = new Name(request.name());
        Description description = new Description(request.description());
        
        // Проверка на дублирование имени
        if (!artist.getName().getValue().equals(name.getValue()) && 
                artistRepository.existsByName(name)) {
            throw new IllegalArgumentException("Artist with name '" + name.getValue() + "' already exists");
        }
        
        artist.updateName(name);
        artist.updateDescription(description);
        Artist saved = artistRepository.save(artist);
        
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ArtistResponse getArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + id));
        
        return mapToResponse(artist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtistResponse> getAllArtists() {
        return artistRepository.findAll().stream()
                .map(this::mapToResponse)
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
    
    private ArtistResponse mapToResponse(Artist artist) {
        List<GenreResponse> genreResponses = artist.getGenres().stream()
                .map(genre -> new GenreResponse(genre.getId(), genre.getName().getValue()))
                .collect(Collectors.toList());
        
        return new ArtistResponse(
                artist.getId(),
                artist.getName().getValue(),
                artist.getDescription() != null ? artist.getDescription().getValue() : null,
                genreResponses
        );
    }
} 