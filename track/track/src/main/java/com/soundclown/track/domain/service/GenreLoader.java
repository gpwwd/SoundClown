package com.soundclown.track.domain.service;

import com.soundclown.track.domain.model.Genre;
import java.util.Collection;
import java.util.List;

public interface GenreLoader {
    List<Genre> loadGenres(Collection<Long> genreIds);
} 