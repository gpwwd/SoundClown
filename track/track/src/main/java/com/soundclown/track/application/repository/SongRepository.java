package com.soundclown.track.application.repository;

import com.soundclown.track.domain.model.Album;
import com.soundclown.track.domain.model.Artist;
import com.soundclown.track.domain.model.Song;
import com.soundclown.track.domain.valueobject.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByTitleAndArtist(Title title, Artist artist);
    List<Song> findByArtist(Artist artist);
    List<Song> findByAlbum(Album album);
} 