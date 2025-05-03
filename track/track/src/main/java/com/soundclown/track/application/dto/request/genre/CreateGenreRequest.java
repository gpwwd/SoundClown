package com.soundclown.track.application.dto.request.genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateGenreRequest(
    @NotBlank(message = "Genre name cannot be blank")
    @Size(max = 100, message = "Genre name cannot be longer than 100 characters")
    String name
) {} 