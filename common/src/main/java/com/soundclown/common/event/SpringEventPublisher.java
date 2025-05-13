package com.soundclown.common.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringEventPublisher implements EventPublisher {
    
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishAudioUploaded(AudioUploadedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
} 