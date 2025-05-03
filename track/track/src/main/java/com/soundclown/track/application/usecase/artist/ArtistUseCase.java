package com.soundclown.track.application.usecase.artist;

import com.soundclown.track.application.dto.request.artist.CreateArtistRequest;
import com.soundclown.track.application.dto.request.artist.UpdateArtistRequest;
import com.soundclown.track.application.dto.response.artist.ArtistResponse;

import java.util.List;

public interface ArtistUseCase {
    ArtistResponse createArtist(CreateArtistRequest request);
    ArtistResponse updateArtist(Long id, UpdateArtistRequest request);
    ArtistResponse getArtistById(Long id);
    List<ArtistResponse> getAllArtists();
    void deleteArtist(Long id);
    void addGenreToArtist(Long artistId, Long genreId);
    void removeGenreFromArtist(Long artistId, Long genreId);
} 