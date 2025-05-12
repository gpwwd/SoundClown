package com.soundclown.storage.application.usecase;

import com.soundclown.storage.application.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UploadImageFileUseCase {
    FileUploadResponse uploadArtistImage(MultipartFile file, Long artistId);
    FileUploadResponse uploadAlbumCover(MultipartFile file, Long albumId);
} 