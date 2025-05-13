package com.soundclown.track.application.mapper;

import com.soundclown.track.application.dto.response.SongResponse;
import com.soundclown.track.domain.model.Song;
import com.soundclown.track.domain.model.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;
@Component
public class SongMapper {

    public SongResponse toResponse(Song song) {
        List<Long> genreIds = song.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        return SongResponse.builder()
                .id(song.getId())
                .audioMetadataId(song.getAudioMetadataId())
                .status(song.getStatus())
                .title(song.getTitle().getValue())
                .durationInSeconds(song.getDuration().getSeconds())
                .releaseDate(song.getReleaseDate())
                .lyrics(song.getLyrics())
                .albumId(song.getAlbum().getId())
                .artistId(song.getArtist().getId())
                .genreIds(new HashSet<>(genreIds))
                .build();
    }

    public List<SongResponse> toResponseList(List<Song> songs) {
        return songs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 