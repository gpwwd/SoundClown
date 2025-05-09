package com.soundclown.track.domain.service;

import com.soundclown.track.domain.valueobject.Name;

public interface GenreNameUniquenessChecker {
    boolean isNameUnique(Name name);
} 