package com.soundclown.track.domain.model;

import com.soundclown.track.domain.valueobject.Name;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres", schema = "track")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Genre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    @Getter
    private Name name;
    
    @ManyToMany(mappedBy = "genres")
    private Set<Artist> artists = new HashSet<>();
    
    @ManyToMany(mappedBy = "genres")
    private Set<Album> albums = new HashSet<>();
    
    @ManyToMany(mappedBy = "genres")
    private Set<Song> songs = new HashSet<>();
    
    public static Genre create(Name name) {
        Genre genre = new Genre();
        genre.name = name;
        return genre;
    }
    
    public void updateName(Name name) {
        this.name = name;
    }
    
    // Вспомогательные методы для работы с коллекциями
    
    public void addArtist(Artist artist) {
        artists.add(artist);
        artist.getGenres().add(this);
    }
    
    public void removeArtist(Artist artist) {
        artists.remove(artist);
        artist.getGenres().remove(this);
    }
    
    public void addAlbum(Album album) {
        albums.add(album);
        album.getGenres().add(this);
    }
    
    public void removeAlbum(Album album) {
        albums.remove(album);
        album.getGenres().remove(this);
    }
    
    public void addSong(Song song) {
        songs.add(song);
        song.getGenres().add(this);
    }
    
    public void removeSong(Song song) {
        songs.remove(song);
        song.getGenres().remove(this);
    }
} 