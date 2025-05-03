package com.soundclown.track.application.service;

import com.soundclown.track.application.dto.request.album.CreateAlbumRequest;
import com.soundclown.track.application.dto.request.album.UpdateAlbumRequest;
import com.soundclown.track.application.dto.response.album.AlbumResponse;
import com.soundclown.track.application.dto.response.artist.ArtistResponse;
import com.soundclown.track.application.dto.response.genre.GenreResponse;
import com.soundclown.track.application.repository.AlbumRepository;
import com.soundclown.track.application.repository.ArtistRepository;
import com.soundclown.track.application.repository.GenreRepository;
import com.soundclown.track.application.usecase.album.AlbumUseCase;
import com.soundclown.track.domain.model.Album;
import com.soundclown.track.domain.model.Artist;
import com.soundclown.track.domain.model.Genre;
import com.soundclown.track.domain.valueobject.Description;
import com.soundclown.track.domain.valueobject.Title;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService implements AlbumUseCase {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;

    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository, GenreRepository genreRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public AlbumResponse createAlbum(CreateAlbumRequest request) {
        Title title = new Title(request.title());
        Description description = new Description(request.description());
        
        Artist artist = artistRepository.findById(request.artistId())
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + request.artistId()));
        
        Album album = Album.create(title, request.releaseDate(), description, artist);
        
        // Добавляем жанры, если они указаны
        if (request.genreIds() != null && !request.genreIds().isEmpty()) {
            for (Long genreId : request.genreIds()) {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
                album.addGenre(genre);
            }
        }
        
        Album saved = albumRepository.save(album);
        
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public AlbumResponse updateAlbum(Long id, UpdateAlbumRequest request) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + id));
        
        Title title = new Title(request.title());
        Description description = new Description(request.description());
        
        album.updateTitle(title);
        album.updateReleaseDate(request.releaseDate());
        album.updateDescription(description);
        
        // Обновляем жанры, если они указаны
        if (request.genreIds() != null) {
            // Очищаем существующие жанры
            new ArrayList<>(album.getGenres()).forEach(album::removeGenre);
            
            // Добавляем новые жанры
            for (Long genreId : request.genreIds()) {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
                album.addGenre(genre);
            }
        }
        
        Album saved = albumRepository.save(album);
        
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AlbumResponse getAlbumById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + id));
        
        return mapToResponse(album);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponse> getAllAlbums() {
        return albumRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponse> getAlbumsByArtistId(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistId));
        
        return albumRepository.findByArtist(artist).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAlbum(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + id));
        
        albumRepository.delete(album);
    }

    @Override
    @Transactional
    public void addGenreToAlbum(Long albumId, Long genreId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + albumId));
        
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
        
        album.addGenre(genre);
        albumRepository.save(album);
    }

    @Override
    @Transactional
    public void removeGenreFromAlbum(Long albumId, Long genreId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + albumId));
        
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
        
        album.removeGenre(genre);
        albumRepository.save(album);
    }
    
    private AlbumResponse mapToResponse(Album album) {
        List<GenreResponse> genreResponses = album.getGenres().stream()
                .map(genre -> new GenreResponse(genre.getId(), genre.getName().getValue()))
                .collect(Collectors.toList());
        
        ArtistResponse artistResponse = new ArtistResponse(
                album.getArtist().getId(),
                album.getArtist().getName().getValue(),
                album.getArtist().getDescription() != null ? album.getArtist().getDescription().getValue() : null,
                List.of() // Не включаем жанры артиста, чтобы избежать циклических зависимостей
        );
        
        return new AlbumResponse(
                album.getId(),
                album.getTitle().getValue(),
                album.getReleaseDate(),
                album.getDescription() != null ? album.getDescription().getValue() : null,
                artistResponse,
                genreResponses
        );
    }
} 