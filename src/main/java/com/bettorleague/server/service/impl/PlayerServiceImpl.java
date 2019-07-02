package com.bettorleague.server.service.impl;

import com.bettorleague.server.dto.contest.MessageRequest;
import com.bettorleague.server.dto.contest.PronosticRequest;
import com.bettorleague.server.exception.BadRequestException;
import com.bettorleague.server.exception.ResourceNotFoundException;
import com.bettorleague.server.model.bettor.*;
import com.bettorleague.server.model.football.Match;
import com.bettorleague.server.model.security.User;
import com.bettorleague.server.repository.bettor.ContestRepository;
import com.bettorleague.server.repository.bettor.MessageRepository;
import com.bettorleague.server.repository.bettor.PlayerRepository;
import com.bettorleague.server.repository.bettor.PronosticRepository;
import com.bettorleague.server.repository.football.MatchRepository;
import com.bettorleague.server.repository.security.UserRepository;
import com.bettorleague.server.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContestRepository contestRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    PronosticRepository pronosticRepository;

    @Autowired
    MatchRepository matchRepository;

    public List<Player> getAll(){
        return this.playerRepository.findAll();
    }

    public Player create(Long contestId, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Contest", "id", contestId));

        contest.getPlayers().stream()
                .filter(player -> player.getUser().getId().equals(userId))
                .findAny()
                .ifPresent(item -> {
                    throw new BadRequestException(user.getName()+" already playing in this contest");
                });

        contestRepository.save(contest);

        Player player = new Player();
        player.setUser(user);
        player.setContest(contest);
        player = playerRepository.save(player);

        return player;
    }

    public Player delete(Long playerId){

        Player player = playerRepository.findById(playerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Player", "id", playerId));

        Contest contest = contestRepository.getOne(player.getContest().getId());

        if(contest.getOwner().getId().equals(playerId)){
            contestRepository.deleteById(contest.getId());
        }else{
            playerRepository.deleteById(playerId);
        }

        return player;
    }



    public List<Pronostic> getPronostics(Long playerId){

        Player player = playerRepository.findById(playerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Player", "id", playerId));

        return player.getPronostics();
    }

    public List<Message> getMessages(Long playerId){

        Player player = playerRepository.findById(playerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Player", "id", playerId));
        return player.getMessages();
    }

    public Message deleteMessage(Long messageId){
        Message message = messageRepository.findById(messageId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Message", "id", messageId));
        messageRepository.deleteById(messageId);
        return message;
    }

    public Message postMessage(Long playerId,MessageRequest message){
        Player player = playerRepository.findById(playerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Player", "id", playerId));

        Message newMessage = new Message();
        newMessage.setContent(message.getContent());
        newMessage.setPlayer(player);
        newMessage.setDate(new Date());
        newMessage.setType(MessageType.CHAT);
        newMessage.setContest(player.getContest());

        return this.messageRepository.save(newMessage);
    }

    public Pronostic savePronostic(Long playerId, PronosticRequest pronosticRequest){

        Player player = playerRepository.findById(playerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Player", "id", playerId));
        Match match = matchRepository.findById(pronosticRequest.getMatchId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Match", "id", pronosticRequest.getMatchId()));

        if (!match.getStatus().equals("SCHEDULED")){
            throw new BadRequestException("Match done");
        }
        if(!match.getCompetition().getId().equals(player.getContest().getCompetition().getId())){
            throw new BadRequestException("Player cant pronostic another competition");
        }

        Optional<Pronostic> pronosticOptional = pronosticRepository.findByMatchIdAndPlayerId(pronosticRequest.getMatchId(),playerId);
        Pronostic pronostic;
        if(pronosticOptional.isPresent()){
            pronostic = pronosticOptional.get();
            pronostic.setDate(new Date());
            pronostic.setResult(pronosticRequest.getResult());
        }else {
            pronostic = new Pronostic();
            pronostic.setResult(pronosticRequest.getResult());
            pronostic.setMatch(match);
            pronostic.setDate(new Date());
            pronostic.setPlayer(player);
        }
        pronostic = pronosticRepository.save(pronostic);
        return pronostic;
    }

    public Pronostic deletePronostic(Long pronosticId){
        Pronostic pronostic = pronosticRepository.findById(pronosticId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Pronostic", "id", pronosticId));
        pronosticRepository.deleteById(pronosticId);
        return pronostic;
    }


}
