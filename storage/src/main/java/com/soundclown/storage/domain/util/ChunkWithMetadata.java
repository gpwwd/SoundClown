package com.soundclown.storage.domain.util;

import com.soundclown.storage.domain.model.AudioMetadataModel;

public record ChunkWithMetadata(
    AudioMetadataModel metadata,
    byte[] chunk
) {}