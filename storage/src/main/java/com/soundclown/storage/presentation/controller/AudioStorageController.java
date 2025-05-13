package com.soundclown.storage.presentation.controller;

import com.soundclown.common.event.AudioUploadedEvent;
import com.soundclown.common.event.EventPublisher;
import com.soundclown.common.dto.AudioMetadataDto;
import com.soundclown.storage.application.dto.response.FileUploadResponse;
import com.soundclown.storage.application.usecase.StreamFileUseCase;
import com.soundclown.storage.application.usecase.UploadAudioFileUseCase;
import com.soundclown.storage.domain.util.Range;
import com.soundclown.storage.domain.util.ChunkWithMetadata;
import com.soundclown.storage.presentation.util.HttpHeadersUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/storage/audio")
@RequiredArgsConstructor
public class AudioStorageController {

    private final UploadAudioFileUseCase uploadFileUseCase;
    private final StreamFileUseCase streamFileUseCase;
    private final EventPublisher eventPublisher;

    @Value("${photon.streaming.default-chunk-size}")
    private Integer defaultChunkSize;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<FileUploadResponse> uploadAudioFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("trackId") Long trackId) {
        FileUploadResponse response = uploadFileUseCase.uploadAudioFile(file, trackId);
        
        AudioMetadataDto metadataDto = AudioMetadataDto.builder()
                .id(response.getId())
                .path(response.getPath())
                .size(response.getSize())
                .httpContentType(response.getHttpContentType())
                .songId(response.getSongId())
                .build();
                
        eventPublisher.publishAudioUploaded(new AudioUploadedEvent(metadataDto));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stream/{trackMetadataId}")
    @PreAuthorize("hasAnyAuthority('CLIENT_BASIC', 'CLIENT_PLUS', 'CLIENT_PRO')")
    public ResponseEntity<byte[]> streamAudio(
            @RequestHeader(value = "Range", required = false) String rangeHeader,
            @PathVariable UUID trackMetadataId) {
        
        Range parsedRange = Range.parseHttpRangeString(rangeHeader, defaultChunkSize);
        ChunkWithMetadata chunkWithMetadata = streamFileUseCase.fetchChunk(trackMetadataId, parsedRange);

        var fileMetadata = chunkWithMetadata.metadata().getFileMetadata();
        
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, fileMetadata.contentType())
                .header(HttpHeaders.CONTENT_LENGTH, HttpHeadersUtil.
                        calculateContentLength(parsedRange, fileMetadata.size()))
                .header(HttpHeaders.CONTENT_RANGE, HttpHeadersUtil.
                        constructContentRange(parsedRange, fileMetadata.size()))
                .body(chunkWithMetadata.chunk());
    }
} 