package com.soundclown.storage.domain.util;

import com.soundclown.storage.domain.model.AudioMetadataEntity;

public record ChunkWithMetadata(
    AudioMetadataEntity metadata,
    byte[] chunk
) {}