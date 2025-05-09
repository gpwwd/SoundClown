package com.soundclown.track.application.dto.response;

import java.time.LocalDate;
import java.util.List;

public record SongResponse(
    Long id,
    String title,
    String duration,
    int durationInSeconds,
    LocalDate releaseDate,
    String lyrics,
    Long albumId,
    Long artistId,
    List<Long> genreIds
) {} 