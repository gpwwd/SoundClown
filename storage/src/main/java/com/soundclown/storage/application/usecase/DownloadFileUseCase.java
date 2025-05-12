package com.soundclown.storage.application.usecase;

import java.util.UUID;

import org.springframework.core.io.Resource;

public interface DownloadFileUseCase {
    Resource downloadFile(UUID fileUuid);
} 