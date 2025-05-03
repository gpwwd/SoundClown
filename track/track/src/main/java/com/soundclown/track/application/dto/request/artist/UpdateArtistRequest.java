package com.soundclown.track.application.dto.request.artist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateArtistRequest(
    @NotBlank(message = "Artist name cannot be blank")
    @Size(max = 255, message = "Artist name cannot be longer than 255 characters")
    String name,
    
    String description
) {} 