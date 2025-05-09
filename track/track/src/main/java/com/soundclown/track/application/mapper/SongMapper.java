package com.soundclown.track.application.mapper;

import com.soundclown.track.application.dto.response.SongResponse;
import com.soundclown.track.domain.model.Song;
import com.soundclown.track.domain.model.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SongMapper {

    public SongResponse toResponse(Song song) {
        if (song == null) {
            return null;
        }

        List<Long> genreIds = song.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        return new SongResponse(
                song.getId(),
                song.getTitle().getValue(),
                song.getDuration().formatMinutesSeconds(),
                song.getDuration().getSeconds(),
                song.getReleaseDate(),
                song.getLyrics(),
                song.getAlbum().getId(),
                song.getArtist().getId(),
                genreIds
        );
    }

    public List<SongResponse> toResponseList(List<Song> songs) {
        if (songs == null) {
            return List.of();
        }

        return songs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 