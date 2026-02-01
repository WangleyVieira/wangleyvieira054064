package com.wangley.musicapi.websocket;

import com.wangley.musicapi.dto.request.AlbumCreatedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class AlbumEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public AlbumEventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishAlbumCreated(Long albumId, String name) {
        messagingTemplate.convertAndSend(
                "/topic/albums",
                new AlbumCreatedEvent(albumId, name)
        );
    }
}
