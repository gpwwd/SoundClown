package com.soundclown.track.presentation.api;

import com.soundclown.track.application.dto.request.artist.CreateArtistRequest;
import com.soundclown.track.application.dto.request.artist.UpdateArtistRequest;
import com.soundclown.track.application.dto.response.artist.ArtistResponse;
import com.soundclown.track.application.usecase.artist.ArtistUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistUseCase artistUseCase;

    @PostMapping
    public ResponseEntity<ArtistResponse> createArtist(@Valid @RequestBody CreateArtistRequest request) {
        return new ResponseEntity<>(artistUseCase.createArtist(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistResponse> updateArtist(
            @PathVariable Long id,
            @Valid @RequestBody UpdateArtistRequest request) {
        return ResponseEntity.ok(artistUseCase.updateArtist(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponse> getArtistById(@PathVariable Long id) {
        return ResponseEntity.ok(artistUseCase.getArtistById(id));
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponse>> getAllArtists() {
        return ResponseEntity.ok(artistUseCase.getAllArtists());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistUseCase.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{artistId}/genres/{genreId}")
    public ResponseEntity<Void> addGenreToArtist(
            @PathVariable Long artistId, 
            @PathVariable Long genreId) {
        artistUseCase.addGenreToArtist(artistId, genreId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{artistId}/genres/{genreId}")
    public ResponseEntity<Void> removeGenreFromArtist(
            @PathVariable Long artistId, 
            @PathVariable Long genreId) {
        artistUseCase.removeGenreFromArtist(artistId, genreId);
        return ResponseEntity.noContent().build();
    }
} 