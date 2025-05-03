package com.soundclown.track.application.dto.response.album;

import com.soundclown.track.application.dto.response.artist.ArtistResponse;
import com.soundclown.track.application.dto.response.genre.GenreResponse;

import java.time.LocalDate;
import java.util.List;

public record AlbumResponse(
    Long id,
    String title,
    LocalDate releaseDate,
    String description,
    ArtistResponse artist,
    List<GenreResponse> genres
) {} 