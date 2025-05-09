package com.soundclown.track.application.dto.response;

import java.util.List;

public record ArtistResponse(
    Long id,
    String name,
    String description,
    List<Long> genreIds
) {} 