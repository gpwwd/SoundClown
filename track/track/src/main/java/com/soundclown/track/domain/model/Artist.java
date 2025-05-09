package com.soundclown.track.domain.model;

import com.soundclown.track.domain.service.ArtistNameUniquenessChecker;
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
    
    @Column(name = "user_id", nullable = false, unique = true)
    @Getter(AccessLevel.PACKAGE)
    private Long userId;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name"))
    private Name name;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "description"))
    private Description description;
    
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albums = new ArrayList<>();
    
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Song> songs = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "artist_genre",
        schema = "track",
        joinColumns = @JoinColumn(name = "artist_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();
    
    private Artist(Long userId, Name name, Description description) {
        this.userId = userId;
        this.name = name;
        this.description = description;
    }
    
    public static Artist create(Long userId, String nameStr, String descriptionStr, ArtistNameUniquenessChecker uniquenessChecker) {
        Name name = new Name(nameStr);
        
        if (!uniquenessChecker.isNameUnique(name)) {
            throw new IllegalArgumentException("Artist with name '" + nameStr + "' already exists");
        }
        
        Description description = new Description(descriptionStr);
        return new Artist(userId, name, description);
    }
    
    public void update(String nameStr, String descriptionStr, ArtistNameUniquenessChecker uniquenessChecker) {
        Name newName = new Name(nameStr);
        
        if (!uniquenessChecker.isNameUnique(newName)) {
            throw new IllegalArgumentException("Artist name '" + nameStr + "' is already taken");
        }
        
        this.name = newName;
        this.description = new Description(descriptionStr);
    }
    
    public String getName() {
        return name.getValue();
    }
    
    public String getDescription() {
        return description != null ? description.getValue() : null;
    }
    
    public boolean belongsToUser(Long userId) {
        return this.userId.equals(userId);
    }
    
    public Set<Genre> getGenres() {
        return new HashSet<>(genres);
    }
    
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
    }
    
    public void removeSong(Song song) {
        songs.remove(song);
    }
    
    public void addGenre(Genre genre) {
        genres.add(genre);
    }
    
    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }
} 