package com.soundclown.storage.application.repository.storage;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.soundclown.storage.domain.model.ImageMetadataModel;

@Repository
public interface ArtistImageFileMetadataRepository extends JpaRepository<ImageMetadataModel, UUID> {
    
}
