package com.soundclown.track.application.mapper;

import com.soundclown.track.application.dto.response.AlbumResponse;
import com.soundclown.track.domain.model.Album;
import com.soundclown.track.domain.model.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlbumMapper {

    public AlbumResponse toResponse(Album album) {
        if (album == null) {
            return null;
        }

        List<Long> genreIds = album.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        return new AlbumResponse(
                album.getId(),
                album.getTitle().getValue(),
                album.getReleaseDate(),
                album.getDescription() != null ? album.getDescription().getValue() : null,
                album.getArtist().getId(),
                genreIds
        );
    }

    public List<AlbumResponse> toResponseList(List<Album> albums) {
        if (albums == null) {
            return List.of();
        }

        return albums.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 