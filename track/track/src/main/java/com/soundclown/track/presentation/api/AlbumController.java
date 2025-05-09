package com.soundclown.track.presentation.api;

import com.soundclown.track.application.dto.request.album.CreateAlbumRequest;
import com.soundclown.track.application.dto.request.album.UpdateAlbumRequest;
import com.soundclown.track.application.dto.response.AlbumResponse;
import com.soundclown.track.application.usecase.AlbumUseCase;
import com.soundclown.common.CurrentUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumUseCase albumUseCase;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<AlbumResponse> createAlbum(
            @CurrentUser Long userId,
            @Valid @RequestBody CreateAlbumRequest request) {
        return new ResponseEntity<>(albumUseCase.createAlbum(request, userId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<AlbumResponse> updateAlbum(
            @PathVariable Long id,
            @CurrentUser Long userId,
            @Valid @RequestBody UpdateAlbumRequest request) {
        return ResponseEntity.ok(albumUseCase.updateAlbum(id, request, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> getAlbumById(@PathVariable Long id) {
        return ResponseEntity.ok(albumUseCase.getAlbumById(id));
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponse>> getAllAlbums() {
        return ResponseEntity.ok(albumUseCase.getAllAlbums());
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<AlbumResponse>> getAlbumsByArtistId(@PathVariable Long artistId) {
        return ResponseEntity.ok(albumUseCase.getAlbumsByArtistId(artistId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<Void> deleteAlbum(
            @PathVariable Long id,
            @CurrentUser Long userId) {
        albumUseCase.deleteAlbum(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{albumId}/genres/{genreId}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<Void> addGenreToAlbum(
            @PathVariable Long albumId,
            @PathVariable Long genreId,
            @CurrentUser Long userId) {
        albumUseCase.addGenreToAlbum(albumId, genreId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{albumId}/genres/{genreId}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<Void> removeGenreFromAlbum(
            @PathVariable Long albumId,
            @PathVariable Long genreId,
            @CurrentUser Long userId) {
        albumUseCase.removeGenreFromAlbum(albumId, genreId, userId);
        return ResponseEntity.noContent().build();
    }
} 