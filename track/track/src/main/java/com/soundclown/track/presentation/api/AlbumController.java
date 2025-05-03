package com.soundclown.track.presentation.api;

import com.soundclown.track.application.dto.request.album.CreateAlbumRequest;
import com.soundclown.track.application.dto.request.album.UpdateAlbumRequest;
import com.soundclown.track.application.dto.response.album.AlbumResponse;
import com.soundclown.track.application.usecase.album.AlbumUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumUseCase albumUseCase;

    @PostMapping
    public ResponseEntity<AlbumResponse> createAlbum(@Valid @RequestBody CreateAlbumRequest request) {
        return new ResponseEntity<>(albumUseCase.createAlbum(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponse> updateAlbum(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAlbumRequest request) {
        return ResponseEntity.ok(albumUseCase.updateAlbum(id, request));
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
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumUseCase.deleteAlbum(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{albumId}/genres/{genreId}")
    public ResponseEntity<Void> addGenreToAlbum(
            @PathVariable Long albumId, 
            @PathVariable Long genreId) {
        albumUseCase.addGenreToAlbum(albumId, genreId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{albumId}/genres/{genreId}")
    public ResponseEntity<Void> removeGenreFromAlbum(
            @PathVariable Long albumId, 
            @PathVariable Long genreId) {
        albumUseCase.removeGenreFromAlbum(albumId, genreId);
        return ResponseEntity.noContent().build();
    }
} 