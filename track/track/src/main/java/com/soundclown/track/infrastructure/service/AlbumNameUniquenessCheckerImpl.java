package com.soundclown.track.infrastructure.service;

import com.soundclown.track.application.repository.AlbumRepository;
import com.soundclown.track.domain.service.AlbumNameUniquenessChecker;
import com.soundclown.track.domain.valueobject.Title;
import org.springframework.stereotype.Service;

@Service
class AlbumNameUniquenessCheckerImpl implements AlbumNameUniquenessChecker {
    private final AlbumRepository albumRepository;

    AlbumNameUniquenessCheckerImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public boolean isNameUnique(Title title) {
        return !albumRepository.existsByTitle(title);
    }
}
