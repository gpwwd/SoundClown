package com.soundclown.storage.application.usecase;

import com.soundclown.storage.application.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadAudioFileUseCase {
    FileUploadResponse uploadAudioFile(MultipartFile file, Long trackId);
} 