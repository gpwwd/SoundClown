package com.soundclown.track.application.dto.request.album;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreateAlbumRequest(
    @NotBlank(message = "Album title cannot be blank")
    @Size(max = 255, message = "Album title cannot be longer than 255 characters")
    String title,
    
    LocalDate releaseDate,
    
    String description,
    
    @NotNull(message = "Artist ID cannot be null")
    Long artistId,
    
    List<Long> genreIds
) {} 