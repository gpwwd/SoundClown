package com.soundclown.storage.application.repository.track;

import com.soundclown.storage.domain.model.SongValidationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongValidationRepository extends JpaRepository<SongValidationModel, Long> {
    Optional<SongValidationModel> findById(Long id);
} 