package com.soundclown.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioMetadataDto {
    private UUID id;
    private String path;
    private long size;
    private String httpContentType;
    private Long songId;
} 