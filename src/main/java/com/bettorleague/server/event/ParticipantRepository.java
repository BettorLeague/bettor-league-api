package com.bettorleague.server.event;

import com.bettorleague.server.model.bettor.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ParticipantRepository {
    private Map<Long, Set<Player>> activeSessions = new ConcurrentHashMap<>();

    public void add(Long contestId, Player player) {
        if(activeSessions.containsKey(contestId)){
            activeSessions.get(contestId).add(player);
        }else{
            Set<Player> players = new HashSet<>();
            players.add(player);
            activeSessions.put(contestId,players);
        }

    }

    public Set<Player> getParticipant(Long contestId) {
        return activeSessions.get(contestId);
    }

    public void removeParticipant(Long contestId,Player player) {
        activeSessions.get(contestId).remove(player);
    }

    public Map<Long, Set<Player>> getActiveSessions() {
        return activeSessions;
    }

    public void setActiveSessions(Map<Long, Set<Player>> activeSessions) {
        this.activeSessions = activeSessions;
    }
}
