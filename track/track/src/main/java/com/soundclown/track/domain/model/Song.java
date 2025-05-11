package com.soundclown.track.domain.model;

import com.soundclown.track.domain.service.GenreLoader;
import com.soundclown.track.domain.valueobject.Duration;
import com.soundclown.track.domain.valueobject.Title;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "songs", schema = "track")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Song {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title"))
    @Getter
    private Title title;
    
    @Embedded
    @AttributeOverride(name = "seconds", column = @Column(name = "duration"))
    @Getter
    private Duration duration;
    
    @Column(name = "release_date")
    @Getter
    private LocalDate releaseDate;
    
    @Column(name = "lyrics")
    @Getter
    private String lyrics;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    @Getter
    private Album album;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    @Getter
    private Artist artist;
    
    @ManyToMany
    @JoinTable(
        name = "song_genre",
        schema = "track",
        joinColumns = @JoinColumn(name = "song_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Getter
    private Set<Genre> genres = new HashSet<>();
    
    public static Song create(String titleStr, Integer durationInSeconds, LocalDate releaseDate, 
                              String lyrics, Album album, Artist artist, GenreLoader genreLoader,
                              Collection<Long> genreIds) {
        album.validateAccess(artist.getUserId());
        
        Song song = new Song();
        song.title = new Title(titleStr);
        song.duration = new Duration(durationInSeconds);
        song.releaseDate = releaseDate;
        song.lyrics = lyrics;
        song.album = album;
        song.artist = artist;
        
        album.addSong(song);
        artist.addSong(song);

        song.updateGenres(genreIds, genreLoader);

        return song;
    }
    
    public void update(String titleStr, Integer durationInSeconds,
                      LocalDate releaseDate, String lyrics,
                      List<Long> genreIds, GenreLoader genreLoader) {
        this.validateAccess(artist.getUserId());
        this.title = new Title(titleStr);
        this.duration = new Duration(durationInSeconds);
        this.releaseDate = releaseDate;
        this.lyrics = lyrics;
        
        if (genreIds != null) {
            updateGenres(genreIds, genreLoader);
        }
    }
    
    public void updateGenres(Collection<Long> genreIds, GenreLoader genreLoader) {
        if (genreIds == null) {
            return;
        }
        
        List<Genre> newGenres = genreLoader.loadGenres(genreIds);
        new HashSet<>(this.genres).forEach(this::removeGenre);
        newGenres.forEach(this::addGenre);
    }
    
    private void addGenre(Genre genre) {
        if (genre != null) {
            genres.add(genre);
        }
    }
    
    private void removeGenre(Genre genre) {
        if (genre != null) {
            genres.remove(genre);
        }
    }

    public void validateAccess(Long userId) {
        if (!artist.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to modify this song");
        }
    }

    public void setAlbum(Album album) {
        if (this.album != null && !this.album.equals(album)) {
            this.album.getSongs().remove(this);
        }
        this.album = album;
    }
} 