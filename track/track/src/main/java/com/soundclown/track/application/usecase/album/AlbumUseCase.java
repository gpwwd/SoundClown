package com.soundclown.track.application.usecase.album;

import com.soundclown.track.application.dto.request.album.CreateAlbumRequest;
import com.soundclown.track.application.dto.request.album.UpdateAlbumRequest;
import com.soundclown.track.application.dto.response.album.AlbumResponse;

import java.util.List;

public interface AlbumUseCase {
    AlbumResponse createAlbum(CreateAlbumRequest request);
    AlbumResponse updateAlbum(Long id, UpdateAlbumRequest request);
    AlbumResponse getAlbumById(Long id);
    List<AlbumResponse> getAllAlbums();
    List<AlbumResponse> getAlbumsByArtistId(Long artistId);
    void deleteAlbum(Long id);
    void addGenreToAlbum(Long albumId, Long genreId);
    void removeGenreFromAlbum(Long albumId, Long genreId);
} 