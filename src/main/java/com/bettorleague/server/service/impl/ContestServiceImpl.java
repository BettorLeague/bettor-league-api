package com.bettorleague.server.service.impl;

import com.bettorleague.server.dto.contest.ContestRequest;
import com.bettorleague.server.exception.ResourceNotFoundException;
import com.bettorleague.server.model.bettor.*;
import com.bettorleague.server.model.football.Competition;
import com.bettorleague.server.model.security.User;
import com.bettorleague.server.repository.bettor.ContestRepository;
import com.bettorleague.server.repository.bettor.MessageRepository;
import com.bettorleague.server.repository.bettor.PlayerRepository;
import com.bettorleague.server.repository.football.CompetitionRepository;
import com.bettorleague.server.repository.security.UserRepository;
import com.bettorleague.server.service.ContestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ContestServiceImpl implements ContestService {

    private final ContestRepository contestRepository;
    private final MessageRepository messageRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;

    public ContestServiceImpl(ContestRepository contestRepository,
                              PlayerRepository playerRepository,
                              UserRepository userRepository,
                              MessageRepository messageRepository,
                              CompetitionRepository competitionRepository){
        this.contestRepository = contestRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.competitionRepository = competitionRepository;
    }

    public Page<Contest> getContestPage(ContestType type,Pageable pageable){
        if(type == null) return contestRepository.findAll(pageable);
        else return this.contestRepository.findByType(type,pageable);
    }

    public Page<Message> getMessagePage(Long contestId, Pageable pageable){
        contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("Contest", "id", contestId));
        return this.messageRepository.findAllByContestId(contestId,pageable);
    }

    public List<Contest> getAllContest(ContestType type){
        if(type == null) return contestRepository.findAll();
        else return this.contestRepository.findAllByType(type);
    }

    public Contest addContest(ContestRequest contest, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Competition competition = competitionRepository.findById(contest.getCompetitionId())
                .orElseThrow(() -> new ResourceNotFoundException("Competition", "id", contest.getCompetitionId()));

        Contest result = new Contest();
        result.setCaption(contest.getCaption());
        result.setCompetition(competition);
        result.setType(contest.getType());
        result.setOwner(null);
        result = contestRepository.save(result);
        Player player = new Player();
        player.setContest(result);
        player.setUser(user);
        player = playerRepository.save(player);
        result.setOwner(player);
        result = contestRepository.save(result);

        return result;
    }



    public Contest getContestById(Long contestId){
        return contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("Contest", "id", contestId));
    }

    public Contest deleteContest(Long contestId){
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("Contest", "id", contestId));
        contestRepository.deleteById(contestId);
        return contest;
    }

    public Set<Message> getMessage(Long contestId){
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("Contest", "id", contestId));
        return contest.getMessages();
    }

    public Set<Player> getPlayer(Long contestId){
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("Contest", "id", contestId));
        return contest.getPlayers();
    }

    /*
    public List<Contest> getPlayerContest(ContestType type,Long userId){

        if(userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User", "id", userId);
        }

        List<Player> players = playerRepository.findAllByUserId(userId);
        List<Contest> contests = new ArrayList<>();

        if(type == null){
            players.forEach(player -> {
                contestRepository.findById(player.getContest().getId()).ifPresent(contests::add);
            });
        }
        else {
            players.forEach(player -> {
                if(player.getContest().getType().equals(type)){
                    contestRepository.findById(player.getContest().getId()).ifPresent(contests::add);
                }
            });
        }
        return contests;
    }*/


    public List<StandingPlayer> getStanding(Long contestId){
        List<Player> players = this.playerRepository.findAllByContestIdOrderByPointsDesc(contestId);
        List<StandingPlayer> result = new ArrayList<>();

        for(int i = 1 ; i < players.size()+1; i++){
            StandingPlayer standingPlayer = new StandingPlayer();
            standingPlayer.setPosition(i);
            standingPlayer.setPlayer(players.get(i -1));
            result.add(standingPlayer);
        }

        return result;
    }






}
