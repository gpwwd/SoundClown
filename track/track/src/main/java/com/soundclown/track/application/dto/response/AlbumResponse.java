package com.soundclown.track.application.dto.response;

import java.time.LocalDate;
import java.util.List;

public record AlbumResponse(
    Long id,
    String title,
    LocalDate releaseDate,
    String description,
    Long artistId,
    List<Long> genreIds
) {} 