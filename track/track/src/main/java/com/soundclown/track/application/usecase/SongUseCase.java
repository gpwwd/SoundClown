package com.soundclown.track.application.usecase;

import com.soundclown.track.application.dto.request.song.CreateSongRequest;
import com.soundclown.track.application.dto.request.song.UpdateSongRequest;
import com.soundclown.track.application.dto.response.SongResponse;

import java.util.List;

public interface SongUseCase {
    SongResponse createSong(CreateSongRequest request);
    SongResponse updateSong(Long id, UpdateSongRequest request);
    SongResponse getSongById(Long id);
    List<SongResponse> getAllSongs();
    List<SongResponse> getSongsByArtistId(Long artistId);
    List<SongResponse> getSongsByAlbumId(Long albumId);
    void deleteSong(Long id);
    void addGenreToSong(Long songId, Long genreId);
    void removeGenreFromSong(Long songId, Long genreId);
} 