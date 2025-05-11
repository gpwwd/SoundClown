package com.soundclown.track.application.usecase;

import com.soundclown.track.application.dto.request.song.CreateSongRequest;
import com.soundclown.track.application.dto.request.song.UpdateSongRequest;
import com.soundclown.track.application.dto.response.SongResponse;

import java.util.List;

public interface SongUseCase {
    SongResponse createSong(CreateSongRequest request, Long userId);
    SongResponse updateSong(Long id, UpdateSongRequest request, Long userId);
    SongResponse getSongById(Long id);
    List<SongResponse> getAllSongs();
    List<SongResponse> getSongsByArtistId(Long artistId);
    List<SongResponse> getSongsByAlbumId(Long albumId);
    void deleteSong(Long id, Long userId);
} 