package com.soundclown.track.application.service;

import com.soundclown.track.application.dto.request.album.CreateAlbumRequest;
import com.soundclown.track.application.dto.request.album.UpdateAlbumRequest;
import com.soundclown.track.application.dto.response.AlbumResponse;
import com.soundclown.track.application.mapper.AlbumMapper;
import com.soundclown.track.application.repository.AlbumRepository;
import com.soundclown.track.application.repository.ArtistRepository;
import com.soundclown.track.application.usecase.AlbumUseCase;
import com.soundclown.track.domain.model.Album;
import com.soundclown.track.domain.model.Artist;
import com.soundclown.track.domain.service.AlbumNameUniquenessChecker;
import com.soundclown.track.domain.service.GenreLoader;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlbumService implements AlbumUseCase {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumNameUniquenessChecker albumNameUniquenessChecker;
    private final GenreLoader genreLoader;
    private final AlbumMapper albumMapper;

    public AlbumService(
            AlbumRepository albumRepository, 
            ArtistRepository artistRepository, 
            AlbumNameUniquenessChecker albumNameUniquenessChecker,
            GenreLoader genreLoader,
            AlbumMapper albumMapper) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.albumNameUniquenessChecker = albumNameUniquenessChecker;
        this.genreLoader = genreLoader;
        this.albumMapper = albumMapper;
    }

    @Override
    @Transactional
    public AlbumResponse createAlbum(CreateAlbumRequest request, Long userId) {
        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found for user with id: " + userId));
        
        Album album = Album.create(
            request.title(), 
            request.releaseDate(), 
            request.description(), 
            artist,
            albumNameUniquenessChecker
        );
        
        album.updateGenres(request.genreIds(), genreLoader);
        
        Album saved = albumRepository.save(album);
        
        return albumMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public AlbumResponse updateAlbum(Long id, UpdateAlbumRequest request, Long userId) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + id));
                
        album.validateAccess(userId);
        
        album.update(
            request.title(),
            request.releaseDate(),
            request.description(),
            albumNameUniquenessChecker
        );
        
        album.updateGenres(request.genreIds(), genreLoader);
        
        Album saved = albumRepository.save(album);
        
        return albumMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AlbumResponse getAlbumById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + id));
        
        return albumMapper.toResponse(album);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponse> getAllAlbums() {
        return albumMapper.toResponseList(albumRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlbumResponse> getAlbumsByArtistId(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistId));
        
        return albumMapper.toResponseList(albumRepository.findByArtist(artist));
    }

    @Override
    @Transactional
    public void deleteAlbum(Long id, Long userId) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + id));
                
        album.validateAccess(userId);
        
        albumRepository.delete(album);
    }

    @Override
    @Transactional
    public void addGenreToAlbum(Long albumId, Long genreId, Long userId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + albumId));
                
        album.validateAccess(userId);
        
        album.addGenreById(genreId, genreLoader);
        albumRepository.save(album);
    }

    @Override
    @Transactional
    public void removeGenreFromAlbum(Long albumId, Long genreId, Long userId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + albumId));
                
        album.validateAccess(userId);
        
        album.removeGenreById(genreId, genreLoader);
        albumRepository.save(album);
    }
} 