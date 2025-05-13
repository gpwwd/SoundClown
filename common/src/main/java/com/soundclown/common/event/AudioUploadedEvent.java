package com.soundclown.common.event;

import com.soundclown.common.dto.AudioMetadataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioUploadedEvent {
    private AudioMetadataDto audioMetadata;
} 