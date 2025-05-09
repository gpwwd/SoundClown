package com.soundclown.track.application.repository;

import com.soundclown.track.domain.model.Artist;
import com.soundclown.track.domain.valueobject.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    boolean existsByName(Name name);
    boolean existsByUserId(Long userId);
    Optional<Artist> findByName(Name name);
    Optional<Artist> findByUserId(Long userId);
} 