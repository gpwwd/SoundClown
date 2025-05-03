package com.soundclown.track.application.service;

import com.soundclown.track.application.dto.request.song.CreateSongRequest;
import com.soundclown.track.application.dto.request.song.UpdateSongRequest;
import com.soundclown.track.application.dto.response.album.AlbumResponse;
import com.soundclown.track.application.dto.response.artist.ArtistResponse;
import com.soundclown.track.application.dto.response.genre.GenreResponse;
import com.soundclown.track.application.dto.response.song.SongResponse;
import com.soundclown.track.application.repository.AlbumRepository;
import com.soundclown.track.application.repository.ArtistRepository;
import com.soundclown.track.application.repository.GenreRepository;
import com.soundclown.track.application.repository.SongRepository;
import com.soundclown.track.application.usecase.song.SongUseCase;
import com.soundclown.track.domain.model.Album;
import com.soundclown.track.domain.model.Artist;
import com.soundclown.track.domain.model.Genre;
import com.soundclown.track.domain.model.Song;
import com.soundclown.track.domain.valueobject.Duration;
import com.soundclown.track.domain.valueobject.Title;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService implements SongUseCase {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;

    public SongService(SongRepository songRepository, AlbumRepository albumRepository, 
                       ArtistRepository artistRepository, GenreRepository genreRepository) {
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public SongResponse createSong(CreateSongRequest request) {
        Title title = new Title(request.title());
        Duration duration = new Duration(request.durationInSeconds());
        
        Artist artist = artistRepository.findById(request.artistId())
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + request.artistId()));
        
        Album album = null;
        if (request.albumId() != null) {
            album = albumRepository.findById(request.albumId())
                    .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + request.albumId()));
        }
        
        Song song = Song.create(title, duration, request.releaseDate(), request.lyrics(), album, artist);
        
        // Добавляем жанры, если они указаны
        if (request.genreIds() != null && !request.genreIds().isEmpty()) {
            for (Long genreId : request.genreIds()) {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
                song.addGenre(genre);
            }
        }
        
        Song saved = songRepository.save(song);
        
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public SongResponse updateSong(Long id, UpdateSongRequest request) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
        
        Title title = new Title(request.title());
        Duration duration = new Duration(request.durationInSeconds());
        
        song.updateTitle(title);
        song.updateDuration(duration);
        song.updateReleaseDate(request.releaseDate());
        song.updateLyrics(request.lyrics());
        
        // Обновляем альбом, если он указан
        if (request.albumId() != null) {
            Album album = albumRepository.findById(request.albumId())
                    .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + request.albumId()));
            song.setAlbum(album);
        } else {
            song.setAlbum(null);
        }
        
        // Обновляем жанры, если они указаны
        if (request.genreIds() != null) {
            // Очищаем существующие жанры
            new ArrayList<>(song.getGenres()).forEach(song::removeGenre);
            
            // Добавляем новые жанры
            for (Long genreId : request.genreIds()) {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
                song.addGenre(genre);
            }
        }
        
        Song saved = songRepository.save(song);
        
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SongResponse getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
        
        return mapToResponse(song);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongResponse> getAllSongs() {
        return songRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongResponse> getSongsByArtistId(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistId));
        
        return songRepository.findByArtist(artist).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SongResponse> getSongsByAlbumId(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + albumId));
        
        return songRepository.findByAlbum(album).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSong(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
        
        songRepository.delete(song);
    }

    @Override
    @Transactional
    public void addGenreToSong(Long songId, Long genreId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + songId));
        
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
        
        song.addGenre(genre);
        songRepository.save(song);
    }

    @Override
    @Transactional
    public void removeGenreFromSong(Long songId, Long genreId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + songId));
        
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId));
        
        song.removeGenre(genre);
        songRepository.save(song);
    }
    
    private SongResponse mapToResponse(Song song) {
        // Маппинг жанров
        List<GenreResponse> genreResponses = song.getGenres().stream()
                .map(genre -> new GenreResponse(genre.getId(), genre.getName().getValue()))
                .collect(Collectors.toList());
        
        // Маппинг артиста
        ArtistResponse artistResponse = new ArtistResponse(
                song.getArtist().getId(),
                song.getArtist().getName().getValue(),
                song.getArtist().getDescription() != null ? song.getArtist().getDescription().getValue() : null,
                List.of() // Не включаем жанры артиста, чтобы избежать циклических зависимостей
        );
        
        // Маппинг альбома
        AlbumResponse albumResponse = null;
        if (song.getAlbum() != null) {
            albumResponse = new AlbumResponse(
                    song.getAlbum().getId(),
                    song.getAlbum().getTitle().getValue(),
                    song.getAlbum().getReleaseDate(),
                    song.getAlbum().getDescription() != null ? song.getAlbum().getDescription().getValue() : null,
                    artistResponse, // Повторное использование ответа артиста
                    List.of() // Не включаем жанры альбома, чтобы избежать циклических зависимостей
            );
        }
        
        // Создание ответа песни
        return new SongResponse(
                song.getId(),
                song.getTitle().getValue(),
                song.getDuration().formatMinutesSeconds(),
                song.getDuration().getSeconds(),
                song.getReleaseDate(),
                song.getLyrics(),
                albumResponse,
                artistResponse,
                genreResponses
        );
    }
} 