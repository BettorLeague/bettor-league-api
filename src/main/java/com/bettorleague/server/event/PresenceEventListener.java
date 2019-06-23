package com.bettorleague.server.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class PresenceEventListener {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        //log.info("Received a new web socket connection");
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long playerId = (Long) headerAccessor.getSessionAttributes().get("playerId");
        Long contestId = (Long) headerAccessor.getSessionAttributes().get("contestId");

        if(playerId != null && contestId != null && participantRepository.getActiveSessions() != null) {

            participantRepository.getParticipant(contestId).stream()
                    .filter(player -> player.getId().equals(playerId))
                    .findAny()
                    .ifPresent(player -> {
                        participantRepository.removeParticipant(contestId,player);
                    });


            //log.info("User Disconnected : " + playerId);

            messagingTemplate.convertAndSend("/topic/chat/"+contestId+"/participants", participantRepository.getParticipant(contestId) );
        }
    }

}
