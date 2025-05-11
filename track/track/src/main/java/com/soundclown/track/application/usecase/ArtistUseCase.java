package com.soundclown.track.application.usecase;

import com.soundclown.track.application.dto.request.artist.CreateArtistRequest;
import com.soundclown.track.application.dto.request.artist.UpdateArtistRequest;
import com.soundclown.track.application.dto.response.ArtistResponse;

import java.util.List;

public interface ArtistUseCase {
    ArtistResponse createArtist(CreateArtistRequest request, Long userId);
    ArtistResponse updateArtist(Long userId, UpdateArtistRequest request);
    ArtistResponse getArtistById(Long id);
    ArtistResponse getArtistByUserId(Long userId);
    List<ArtistResponse> getAllArtists();
    void deleteArtist(Long id);
    void addGenreToArtist(Long artistId, Long genreId);
    void removeGenreFromArtist(Long artistId, Long genreId);
} 