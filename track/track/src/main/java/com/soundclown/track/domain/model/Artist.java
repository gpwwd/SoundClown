package com.soundclown.track.domain.model;

import com.soundclown.track.domain.valueobject.Description;
import com.soundclown.track.domain.valueobject.Name;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "artists", schema = "track")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    @Getter
    private Name name;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "description"))
    @Getter
    private Description description;
    
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Album> albums = new ArrayList<>();
    
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Song> songs = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "artist_genre",
        schema = "track",
        joinColumns = @JoinColumn(name = "artist_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Getter
    private Set<Genre> genres = new HashSet<>();
    
    public static Artist create(Name name, Description description) {
        Artist artist = new Artist();
        artist.name = name;
        artist.description = description;
        return artist;
    }
    
    public void updateName(Name name) {
        this.name = name;
    }
    
    public void updateDescription(Description description) {
        this.description = description;
    }
    
    // Методы для работы с коллекциями
    
    public void addAlbum(Album album) {
        albums.add(album);
        album.setArtist(this);
    }
    
    public void removeAlbum(Album album) {
        albums.remove(album);
        album.setArtist(null);
    }
    
    public void addSong(Song song) {
        songs.add(song);
        song.setArtist(this);
    }
    
    public void removeSong(Song song) {
        songs.remove(song);
        song.setArtist(null);
    }
    
    public void addGenre(Genre genre) {
        genres.add(genre);
    }
    
    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }
} 