package com.soundclown.track.application.usecase.genre;

import com.soundclown.track.application.dto.request.genre.CreateGenreRequest;
import com.soundclown.track.application.dto.request.genre.UpdateGenreRequest;
import com.soundclown.track.application.dto.response.genre.GenreResponse;

import java.util.List;

public interface GenreUseCase {
    GenreResponse createGenre(CreateGenreRequest request);
    GenreResponse updateGenre(Long id, UpdateGenreRequest request);
    GenreResponse getGenreById(Long id);
    List<GenreResponse> getAllGenres();
    void deleteGenre(Long id);
} 