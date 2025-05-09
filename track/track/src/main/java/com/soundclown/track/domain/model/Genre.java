package com.soundclown.track.domain.model;

import com.soundclown.track.domain.valueobject.Name;
import com.soundclown.track.domain.service.GenreNameUniquenessChecker;
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
    
    private Genre(Name name) {
        this.name = name;
    }
    
    public static Genre create(String nameStr, GenreNameUniquenessChecker uniquenessChecker) {
        Name name = new Name(nameStr);
        
        if (!uniquenessChecker.isNameUnique(name)) {
            throw new IllegalArgumentException("Genre with name '" + name.getValue() + "' already exists");
        }
        
        return new Genre(name);
    }
    
    public void updateName(String nameStr, GenreNameUniquenessChecker uniquenessChecker) {
        Name newName = new Name(nameStr);
        
        if (!uniquenessChecker.isNameUnique(newName)) {
            throw new IllegalArgumentException("Genre with name '" + newName.getValue() + "' already exists");
        }
        
        this.name = newName;
    }
    
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