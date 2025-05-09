package com.soundclown.track.infrastructure.service;

import com.soundclown.track.application.repository.ArtistRepository;
import com.soundclown.track.domain.service.ArtistNameUniquenessChecker;
import com.soundclown.track.domain.valueobject.Name;
import org.springframework.stereotype.Service;

@Service
class ArtistNameUniquenessCheckerImpl implements ArtistNameUniquenessChecker {
    private final ArtistRepository artistRepository;

    ArtistNameUniquenessCheckerImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public boolean isNameUnique(Name name) {
        return !artistRepository.existsByName(name);
    }
} 