package com.bettorleague.server.service;

import com.bettorleague.server.dto.contest.ContestRequest;
import com.bettorleague.server.model.bettor.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ContestService {

    Page<Message> getMessagePage(Long contestId, Pageable pageable);
    Page<Contest> getContestPage(ContestType type, Pageable pageable);
    List<Contest> getAllContest(ContestType type);

    Contest addContest(ContestRequest contest,Long userId);
    Contest deleteContest(Long contestId);
    Contest getContestById(Long contestId);

    Set<Player> getPlayer(Long contestId);
    List<StandingPlayer>    getStanding(Long contestId);
    Set<Message>           getMessage(Long contestId);


}
