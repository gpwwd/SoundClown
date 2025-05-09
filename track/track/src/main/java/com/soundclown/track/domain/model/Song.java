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
    
    public static Song create(Title title, Duration duration, LocalDate releaseDate, 
                              String lyrics, Album album, Artist artist) {
        Song song = new Song();
        song.title = title;
        song.duration = duration;
        song.releaseDate = releaseDate;
        song.lyrics = lyrics;
        song.album = album;
        song.artist = artist;
        
        album.addSong(song);
        artist.addSong(song);
        
        return song;
    }
    
    public void update(
            Title title,
            Duration duration,
            LocalDate releaseDate,
            String lyrics
    ) {
        this.title = title;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.lyrics = lyrics;
    }
    
    public void updateGenres(Collection<Long> genreIds, GenreLoader genreLoader) {
        if (genreIds == null) {
            return;
        }
        
        List<Genre> newGenres = genreLoader.loadGenres(genreIds);
        new HashSet<>(this.genres).forEach(this::removeGenre);
        newGenres.forEach(this::addGenre);
    }
    
    public void addGenreById(Long genreId, GenreLoader genreLoader) {
        if (genreId == null) {
            return;
        }
        
        List<Genre> loadedGenres = genreLoader.loadGenres(List.of(genreId));
        if (!loadedGenres.isEmpty()) {
            addGenre(loadedGenres.get(0));
        }
    }
    
    public void removeGenreById(Long genreId, GenreLoader genreLoader) {
        if (genreId == null) {
            return;
        }
        
        List<Genre> loadedGenres = genreLoader.loadGenres(List.of(genreId));
        if (!loadedGenres.isEmpty()) {
            removeGenre(loadedGenres.get(0));
        }
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
} 