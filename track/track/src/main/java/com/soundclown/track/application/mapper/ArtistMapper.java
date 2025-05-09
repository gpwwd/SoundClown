package com.soundclown.track.application.mapper;

import com.soundclown.track.application.dto.response.ArtistResponse;
import com.soundclown.track.domain.model.Artist;
import com.soundclown.track.domain.model.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArtistMapper {
    
    public ArtistResponse toResponse(Artist artist) {
        List<Long> genreIds = artist.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
        
        return new ArtistResponse(
                artist.getId(),
                artist.getName(),
                artist.getDescription(),
                genreIds
        );
    }
} 