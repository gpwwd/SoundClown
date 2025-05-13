package com.soundclown.track.infrastructure.event;

import com.soundclown.common.event.AudioUploadedEvent;
import com.soundclown.track.domain.model.Song;
import com.soundclown.track.application.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AudioUploadEventListener {

    private final SongRepository songRepository;

    @EventListener
    @Transactional
    public void handleAudioUploadedEvent(AudioUploadedEvent event) {
        Song song = songRepository.findById(event.getAudioMetadata().getSongId())
                .orElseThrow(() -> new IllegalStateException("Song not found"));
                
        song.setAudioMetadata(event.getAudioMetadata().getId());
        songRepository.save(song);
    }
} 