package com.soundclown.storage.application.usecase;

import com.soundclown.storage.domain.util.Range;
import com.soundclown.storage.domain.util.ChunkWithMetadata;

import java.util.UUID;

public interface StreamFileUseCase {
    ChunkWithMetadata fetchChunk(UUID trackId, Range range);
} 