package com.soundclown.track.infrastructure.service;

import com.soundclown.track.domain.service.GenreNameUniquenessChecker;
import com.soundclown.track.domain.valueobject.Name;
import com.soundclown.track.application.repository.GenreRepository;
import org.springframework.stereotype.Service;

@Service
class GenreNameUniquenessCheckerImpl implements GenreNameUniquenessChecker {
    private final GenreRepository genreRepository;

    GenreNameUniquenessCheckerImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public boolean isNameUnique(Name name) {
        return !genreRepository.existsByName(name);
    }
} 