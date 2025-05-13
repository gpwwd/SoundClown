package com.soundclown.track.application.dto.response;

import com.soundclown.track.domain.model.Song;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class SongResponse {
    private Long id;
    private UUID audioMetadataId;
    private Song.Status status;
    private String title;
    private Integer durationInSeconds;
    private LocalDate releaseDate;
    private String lyrics;
    private Long albumId;
    private Long artistId;
    private Set<Long> genreIds;

    public boolean isPublishable() {
        return status == Song.Status.ACTIVE && audioMetadataId != null;
    }
} 