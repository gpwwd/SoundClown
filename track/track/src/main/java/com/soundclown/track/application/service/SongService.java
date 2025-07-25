package com.soundclown.track.application.service;

import com.soundclown.track.application.dto.request.song.CreateSongRequest;
import com.soundclown.track.application.dto.request.song.UpdateSongRequest;
import com.soundclown.track.application.dto.response.SongResponse;
import com.soundclown.track.application.mapper.SongMapper;
import com.soundclown.track.application.repository.AlbumRepository;
import com.soundclown.track.application.repository.ArtistRepository;
import com.soundclown.track.application.repository.SongRepository;
import com.soundclown.track.application.usecase.SongUseCase;
import com.soundclown.track.domain.model.Album;
import com.soundclown.track.domain.model.Artist;
import com.soundclown.track.domain.model.Song;
import com.soundclown.track.domain.service.GenreLoader;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SongService implements SongUseCase {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final GenreLoader genreLoader;
    private final SongMapper songMapper;

    public SongService(
            SongRepository songRepository, 
            AlbumRepository albumRepository,
            ArtistRepository artistRepository, 
            GenreLoader genreLoader,
            SongMapper songMapper) {
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.genreLoader = genreLoader;
        this.songMapper = songMapper;
    }

    @Override
    @Transactional
    public SongResponse createSong(CreateSongRequest request, Long userId) {
        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found for user with id: " + userId));

        Album album = albumRepository.findById(request.albumId())
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + request.albumId()));

        Song song = Song.create(request.title(), request.durationInSeconds(),
            request.releaseDate(), request.lyrics(),
            album, artist, genreLoader, request.genreIds()
        );

        Song saved = songRepository.save(song);

        return songMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public SongResponse updateSong(Long id, UpdateSongRequest request, Long userId) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));

        song.update(request.title(), request.durationInSeconds(),
            request.releaseDate(), request.lyrics(),
            request.genreIds(), genreLoader
        );
        
        Song saved = songRepository.save(song);
        return songMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SongResponse getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
        
        return songMapper.toResponse(song);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongResponse> getAllSongs() {
        return songMapper.toResponseList(songRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongResponse> getSongsByArtistId(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistId));
        
        return songMapper.toResponseList(songRepository.findByArtist(artist));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongResponse> getSongsByAlbumId(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + albumId));
        
        return songMapper.toResponseList(songRepository.findByAlbum(album));
    }

    @Override
    @Transactional
    public void deleteSong(Long id, Long userId) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
        
        song.validateAccess(userId);
        songRepository.delete(song);
    }
} 