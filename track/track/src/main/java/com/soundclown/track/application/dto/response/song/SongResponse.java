package com.soundclown.track.application.dto.response.song;

import com.soundclown.track.application.dto.response.album.AlbumResponse;
import com.soundclown.track.application.dto.response.artist.ArtistResponse;
import com.soundclown.track.application.dto.response.genre.GenreResponse;

import java.time.LocalDate;
import java.util.List;

public record SongResponse(
    Long id,
    String title,
    String duration,
    int durationInSeconds,
    LocalDate releaseDate,
    String lyrics,
    AlbumResponse album,
    ArtistResponse artist,
    List<GenreResponse> genres
) {} 