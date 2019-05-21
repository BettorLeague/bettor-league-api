package com.bettorleague.server.service;

import com.bettorleague.server.model.football.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchService {
    Page<Match> getAllMatch(Pageable pageable);
    Match getMatchById(Long contestId);
}
