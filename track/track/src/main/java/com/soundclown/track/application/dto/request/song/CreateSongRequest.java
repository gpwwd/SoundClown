package com.soundclown.track.application.dto.request.song;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreateSongRequest(
    @NotBlank(message = "Song title cannot be blank")
    @Size(max = 255, message = "Song title cannot be longer than 255 characters")
    String title,
    
    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be positive")
    Integer durationInSeconds,
    
    LocalDate releaseDate,
    
    String lyrics,
    
    @NotNull(message = "Album ID cannot be null")
    Long albumId,
    
    @NotNull(message = "Artist ID cannot be null")
    Long artistId,
    
    List<Long> genreIds
) {} 