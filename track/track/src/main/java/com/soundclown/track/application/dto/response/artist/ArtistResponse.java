package com.soundclown.track.application.dto.response.artist;

import com.soundclown.track.application.dto.response.genre.GenreResponse;

import java.util.List;

public record ArtistResponse(
    Long id,
    String name,
    String description,
    List<GenreResponse> genres
) {} 