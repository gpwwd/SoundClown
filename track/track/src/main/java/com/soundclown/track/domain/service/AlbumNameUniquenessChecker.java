package com.soundclown.track.domain.service;

import com.soundclown.track.domain.valueobject.Title;

public interface AlbumNameUniquenessChecker {
    boolean isNameUnique(Title title);
}
