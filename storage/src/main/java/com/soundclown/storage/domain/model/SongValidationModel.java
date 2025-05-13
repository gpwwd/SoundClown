package com.soundclown.storage.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "songs", schema = "track")
@NoArgsConstructor
public class SongValidationModel {
    
    public enum Status {
        DRAFT,
        ACTIVE,
        DELETED
    }

    @Id
    @Getter
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Getter
    private Status status;

    public void validateStatusForAudioUpload() {
        if (!Status.DRAFT.equals(this.status)) {
            throw new RuntimeException("Audio file cannot be uploaded for a track with status "
             + this.status);
        }
    }
} 