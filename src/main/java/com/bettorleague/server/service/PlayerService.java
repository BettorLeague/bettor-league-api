package com.bettorleague.server.service;

import com.bettorleague.server.dto.contest.MessageRequest;
import com.bettorleague.server.dto.contest.PronosticRequest;
import com.bettorleague.server.model.bettor.Message;
import com.bettorleague.server.model.bettor.Player;
import com.bettorleague.server.model.bettor.Pronostic;

import java.util.List;

public interface PlayerService {

    List<Player> getAll();
    Player create(Long contestId,Long userId);
    Player delete(Long playerId);

    List<Pronostic>     getPronostics(Long playerId);
    Pronostic           savePronostic(Long playerId, PronosticRequest pronosticRequest);
    Pronostic           deletePronostic(Long pronosticId);

    List<Message>       getMessages(Long playerId);
    Message             postMessage(Long playerId,MessageRequest message);
    Message             deleteMessage(Long messageId);
}
