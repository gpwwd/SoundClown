package com.soundclown.track.application.repository;

import com.soundclown.track.domain.model.Album;
import com.soundclown.track.domain.model.Artist;
import com.soundclown.track.domain.valueobject.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByTitleAndArtist(Title title, Artist artist);
    List<Album> findByArtist(Artist artist);
    boolean existsByTitle(Title title);
} 