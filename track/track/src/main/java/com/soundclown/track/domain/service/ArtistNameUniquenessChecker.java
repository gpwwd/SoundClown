package com.soundclown.track.domain.service;

import com.soundclown.track.domain.valueobject.Name;

public interface ArtistNameUniquenessChecker {
    boolean isNameUnique(Name name);
} 