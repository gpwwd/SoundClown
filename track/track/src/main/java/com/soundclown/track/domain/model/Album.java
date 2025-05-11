package com.soundclown.track.domain.model;

import com.soundclown.track.domain.service.AlbumNameUniquenessChecker;
import com.soundclown.track.domain.service.GenreLoader;
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
import java.util.Collection;

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
    @JoinColumn(name = "artist_id", nullable = false)
    @Getter
    @Setter(AccessLevel.PACKAGE)
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
    
    public static Album create(String titleStr, LocalDate releaseDate, String descriptionStr, 
                              Artist artist, AlbumNameUniquenessChecker uniquenessChecker,
                              Collection<Long> genreIds, GenreLoader genreLoader) {
        Title title = new Title(titleStr);
        Description description = new Description(descriptionStr);
        
        if (!uniquenessChecker.isNameUnique(title)) {
            throw new IllegalArgumentException("Album with title '" + titleStr + "' already exists");
        }
        
        Album album = new Album();
        album.title = title;
        album.releaseDate = releaseDate;
        album.description = description;
        album.artist = artist;
        
        artist.addAlbum(album);

        album.updateGenres(genreIds, genreLoader);
        
        return album;
    }
    
    public void validateAccess(Long userId) {
        if (!artist.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to modify this album");
        }
    }
    
    public void update(String titleStr, LocalDate releaseDate, String descriptionStr, 
                      AlbumNameUniquenessChecker uniquenessChecker, Collection<Long> genreIds,
                      GenreLoader genreLoader) {
        this.validateAccess(artist.getUserId());
        Title newTitle = new Title(titleStr);
        Description newDescription = new Description(descriptionStr);
        
        if (!uniquenessChecker.isNameUnique(newTitle)) {
            throw new IllegalArgumentException("Album with title '" + titleStr + "' already exists");
        }
        
        this.title = newTitle;
        this.releaseDate = releaseDate;
        this.description = newDescription;

        updateGenres(genreIds, genreLoader);
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
    
    public void addSong(Song song) {
        if (songs == null) {
            songs = new ArrayList<>();
        }
        
        if (songs.contains(song)) {
            throw new IllegalArgumentException("Song is already in the album");
        }
        
        songs.add(song);
        song.setAlbum(this);
    }
    
    public void removeSong(Song song) {
        if (!song.getAlbum().equals(this)) {
            throw new IllegalArgumentException("Song is not in this album");
        }
        songs.remove(song);
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