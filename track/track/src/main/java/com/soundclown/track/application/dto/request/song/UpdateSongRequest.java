package com.soundclown.track.application.dto.request.song;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record UpdateSongRequest(
    @NotBlank(message = "Song title cannot be blank")
    @Size(max = 255, message = "Song title cannot be longer than 255 characters")
    String title,
    
    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be positive")
    Integer durationInSeconds,
    
    LocalDate releaseDate,
    
    String lyrics,
    
    Long albumId,
    
    List<Long> genreIds
) {} 