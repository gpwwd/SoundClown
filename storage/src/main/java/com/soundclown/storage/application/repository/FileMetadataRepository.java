package com.soundclown.storage.application.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soundclown.storage.domain.model.AudioMetadataEntity;

@Repository
public interface FileMetadataRepository extends JpaRepository<AudioMetadataEntity, UUID> {
} 