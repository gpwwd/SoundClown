package com.soundclown.track.application.repository;

import com.soundclown.track.domain.model.Genre;
import com.soundclown.track.domain.valueobject.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(Name name);
    boolean existsByName(Name name);
} 