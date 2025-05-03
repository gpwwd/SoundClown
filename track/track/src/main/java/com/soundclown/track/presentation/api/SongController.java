package com.soundclown.track.presentation.api;

import com.soundclown.track.application.dto.request.song.CreateSongRequest;
import com.soundclown.track.application.dto.request.song.UpdateSongRequest;
import com.soundclown.track.application.dto.response.song.SongResponse;
import com.soundclown.track.application.usecase.song.SongUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongUseCase songUseCase;

    @PostMapping
    public ResponseEntity<SongResponse> createSong(@Valid @RequestBody CreateSongRequest request) {
        return new ResponseEntity<>(songUseCase.createSong(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongResponse> updateSong(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSongRequest request) {
        return ResponseEntity.ok(songUseCase.updateSong(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongResponse> getSongById(@PathVariable Long id) {
        return ResponseEntity.ok(songUseCase.getSongById(id));
    }

    @GetMapping
    public ResponseEntity<List<SongResponse>> getAllSongs() {
        return ResponseEntity.ok(songUseCase.getAllSongs());
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<SongResponse>> getSongsByArtistId(@PathVariable Long artistId) {
        return ResponseEntity.ok(songUseCase.getSongsByArtistId(artistId));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<SongResponse>> getSongsByAlbumId(@PathVariable Long albumId) {
        return ResponseEntity.ok(songUseCase.getSongsByAlbumId(albumId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songUseCase.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{songId}/genres/{genreId}")
    public ResponseEntity<Void> addGenreToSong(
            @PathVariable Long songId, 
            @PathVariable Long genreId) {
        songUseCase.addGenreToSong(songId, genreId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{songId}/genres/{genreId}")
    public ResponseEntity<Void> removeGenreFromSong(
            @PathVariable Long songId, 
            @PathVariable Long genreId) {
        songUseCase.removeGenreFromSong(songId, genreId);
        return ResponseEntity.noContent().build();
    }
} 