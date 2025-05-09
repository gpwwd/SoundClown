package com.soundclown.track.presentation.api;

import com.soundclown.track.application.dto.request.genre.CreateGenreRequest;
import com.soundclown.track.application.dto.request.genre.UpdateGenreRequest;
import com.soundclown.track.application.dto.response.GenreResponse;
import com.soundclown.track.application.usecase.GenreUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreUseCase genreUseCase;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGER', 'ADMIN_GOD', 'ADMIN_BASIC')")
    public ResponseEntity<GenreResponse> createGenre(@Valid @RequestBody CreateGenreRequest request) {
        return new ResponseEntity<>(genreUseCase.createGenre(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGER', 'ADMIN_GOD', 'ADMIN_BASIC')")
    public ResponseEntity<GenreResponse> updateGenre(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGenreRequest request) {
        return ResponseEntity.ok(genreUseCase.updateGenre(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok(genreUseCase.getGenreById(id));
    }

    @GetMapping
    public ResponseEntity<List<GenreResponse>> getAllGenres() {
        return ResponseEntity.ok(genreUseCase.getAllGenres());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGER', 'ADMIN_GOD', 'ADMIN_BASIC')")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreUseCase.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
} 