package com.soundclown.track.infrastructure.service;

import com.soundclown.track.application.repository.GenreRepository;
import com.soundclown.track.domain.model.Genre;
import com.soundclown.track.domain.service.GenreLoader;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
class GenreLoaderImpl implements GenreLoader {
    private final GenreRepository genreRepository;

    GenreLoaderImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> loadGenres(Collection<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return List.of();
        }
        
        return genreIds.stream()
            .map(genreId -> genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + genreId)))
            .collect(Collectors.toList());
    }
} 