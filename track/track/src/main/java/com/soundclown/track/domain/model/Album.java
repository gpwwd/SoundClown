package com.soundclown.track.domain.model;

import com.soundclown.track.domain.valueobject.Description;
import com.soundclown.track.domain.valueobject.Title;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "albums", schema = "track")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "title"))
    @Getter
    private Title title;
    
    @Column(name = "release_date")
    @Getter
    private LocalDate releaseDate;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "description"))
    @Getter
    private Description description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    @Getter
    @Setter
    private Artist artist;
    
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Song> songs = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "album_genre",
        schema = "track",
        joinColumns = @JoinColumn(name = "album_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Getter
    private Set<Genre> genres = new HashSet<>();
    
    public static Album create(Title title, LocalDate releaseDate, Description description, Artist artist) {
        Album album = new Album();
        album.title = title;
        album.releaseDate = releaseDate;
        album.description = description;
        album.artist = artist;
        
        if (artist != null) {
            artist.getAlbums().add(album);
        }
        
        return album;
    }
    
    public void updateTitle(Title title) {
        this.title = title;
    }
    
    public void updateReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public void updateDescription(Description description) {
        this.description = description;
    }
    
    // Методы для работы с коллекциями
    
    public void addSong(Song song) {
        songs.add(song);
        song.setAlbum(this);
    }
    
    public void removeSong(Song song) {
        songs.remove(song);
        song.setAlbum(null);
    }
    
    public void addGenre(Genre genre) {
        genres.add(genre);
    }
    
    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }
} 