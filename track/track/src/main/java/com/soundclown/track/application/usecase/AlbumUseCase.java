package com.soundclown.track.application.usecase;

import com.soundclown.track.application.dto.request.album.CreateAlbumRequest;
import com.soundclown.track.application.dto.request.album.UpdateAlbumRequest;
import com.soundclown.track.application.dto.response.AlbumResponse;

import java.util.List;

public interface AlbumUseCase {
    AlbumResponse createAlbum(CreateAlbumRequest request, Long userId);
    AlbumResponse updateAlbum(Long id, UpdateAlbumRequest request, Long userId);
    AlbumResponse getAlbumById(Long id);
    List<AlbumResponse> getAllAlbums();
    List<AlbumResponse> getAlbumsByArtistId(Long artistId);
    void deleteAlbum(Long id, Long userId);
    void addGenreToAlbum(Long albumId, Long genreId, Long userId);
    void removeGenreFromAlbum(Long albumId, Long genreId, Long userId);
    void addSongToAlbum(Long albumId, Long songId, Long userId);
    void removeSongFromAlbum(Long albumId, Long songId, Long userId);
} 