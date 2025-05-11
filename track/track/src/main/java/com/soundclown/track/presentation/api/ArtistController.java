package com.soundclown.track.presentation.api;

import com.soundclown.track.application.dto.request.artist.CreateArtistRequest;
import com.soundclown.track.application.dto.request.artist.UpdateArtistRequest;
import com.soundclown.track.application.dto.response.ArtistResponse;
import com.soundclown.track.application.usecase.ArtistUseCase;
import com.soundclown.common.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistUseCase artistUseCase;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<ArtistResponse> createArtist(
            @Valid @RequestBody CreateArtistRequest request,
            @CurrentUser Long userId) {
        return new ResponseEntity<>(
            artistUseCase.createArtist(request, userId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<ArtistResponse> updateArtist(
            @CurrentUser Long userId,
            @Valid @RequestBody UpdateArtistRequest request) {
        return ResponseEntity.ok(artistUseCase.updateArtist(userId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponse> getArtistById(@PathVariable Long id) {
        return ResponseEntity.ok(artistUseCase.getArtistById(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<ArtistResponse> getArtistByUserId(
            @CurrentUser Long userId) {
        return ResponseEntity.ok(artistUseCase.getArtistByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponse>> getAllArtists() {
        return ResponseEntity.ok(artistUseCase.getAllArtists());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO',"
            + " 'ADMIN_BASIC', 'ADMIN_MANAGER', 'ADMIN_GOD')")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistUseCase.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{artistId}/genres/{genreId}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<Void> addGenreToArtist(
            @PathVariable Long artistId, 
            @PathVariable Long genreId) {
        artistUseCase.addGenreToArtist(artistId, genreId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{artistId}/genres/{genreId}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<Void> removeGenreFromArtist(
            @PathVariable Long artistId, 
            @PathVariable Long genreId) {
        artistUseCase.removeGenreFromArtist(artistId, genreId);
        return ResponseEntity.noContent().build();
    }
} 