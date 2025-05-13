package com.soundclown.track.presentation.api;

import com.soundclown.track.application.dto.request.song.CreateSongRequest;
import com.soundclown.track.application.dto.request.song.UpdateSongRequest;
import com.soundclown.track.application.dto.response.SongResponse;
import com.soundclown.track.application.usecase.SongUseCase;
import com.soundclown.common.CurrentUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/v1/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongUseCase songUseCase;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<SongResponse> createSong(
            @Valid @RequestBody CreateSongRequest request,
            @CurrentUser Long userId) {
        return new ResponseEntity<>(songUseCase.createSong(request, userId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<SongResponse> updateSong(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSongRequest request,
            @CurrentUser Long userId) {
        return ResponseEntity.ok(songUseCase.updateSong(id, request, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongResponse> getSongById(@PathVariable Long id) {
        SongResponse song = songUseCase.getSongById(id);
        if (!song.isPublishable()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(song);
    }

    @GetMapping
    public ResponseEntity<List<SongResponse>> getAllSongs() {
        return ResponseEntity.ok(
            songUseCase.getAllSongs().stream()
                .filter(SongResponse::isPublishable)
                .toList()
        );
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<SongResponse>> getSongsByArtistId(@PathVariable Long artistId) {
        return ResponseEntity.ok(
            songUseCase.getSongsByArtistId(artistId).stream()
                .filter(SongResponse::isPublishable)
                .toList()
        );
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<SongResponse>> getSongsByAlbumId(@PathVariable Long albumId) {
        return ResponseEntity.ok(
            songUseCase.getSongsByAlbumId(albumId).stream()
                .filter(SongResponse::isPublishable)
                .toList()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<Void> deleteSong(
            @PathVariable Long id,
            @CurrentUser Long userId) {
        songUseCase.deleteSong(id, userId);
        return ResponseEntity.noContent().build();
    }
} 