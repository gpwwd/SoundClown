package com.soundclown.storage.application.usecase;

import java.util.UUID;

public interface DeleteFileUseCase {
    void deleteFile(UUID fileUuid);
} 