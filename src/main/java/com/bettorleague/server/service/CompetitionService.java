package com.bettorleague.server.service;


import com.bettorleague.server.model.football.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CompetitionService {

    List<Match> getAllMatchesOfCompetition(Long competitionId, Integer matchDay, StandingStage stage, StandingGroup group);

    List<Standing> getAllStandingsOfCompetition(Long competitionId, StandingType type, StandingGroup group);

}
