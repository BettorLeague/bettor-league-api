package com.bettorleague.server.service.impl;

import com.bettorleague.server.exception.ResourceNotFoundException;
import com.bettorleague.server.model.football.Match;
import com.bettorleague.server.model.security.User;
import com.bettorleague.server.repository.football.MatchRepository;
import com.bettorleague.server.service.MatchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;

    public MatchServiceImpl(MatchRepository matchRepository){
        this.matchRepository = matchRepository;
    }

    @Override
    public Page<Match> getAllMatch(Pageable pageable) {
        return matchRepository.findAll(pageable);
    }

    @Override
    public Match getMatchById(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", "id", matchId));
    }
}
