package com.soundclown.track.domain.model;

import com.soundclown.track.domain.valueobject.Duration;
import com.soundclown.track.domain.valueobject.Title;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @JoinColumn(name = "album_id")
    @Getter
    @Setter
    private Album album;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    @Getter
    @Setter
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
        
        if (album != null) {
            album.getSongs().add(song);
        }
        
        if (artist != null) {
            artist.getSongs().add(song);
        }
        
        return song;
    }
    
    public void updateTitle(Title title) {
        this.title = title;
    }
    
    public void updateDuration(Duration duration) {
        this.duration = duration;
    }
    
    public void updateReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public void updateLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
    
    // Методы для работы с коллекциями
    
    public void addGenre(Genre genre) {
        genres.add(genre);
    }
    
    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }
} 