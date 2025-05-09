package com.soundclown.track.application.mapper;

import com.soundclown.track.application.dto.response.GenreResponse;
import com.soundclown.track.domain.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    
    public GenreResponse toResponse(Genre genre) {
        return new GenreResponse(
                genre.getId(),
                genre.getName().getValue()
        );
    }
} 