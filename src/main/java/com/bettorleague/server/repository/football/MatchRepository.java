package com.bettorleague.server.repository.football;


import com.bettorleague.server.model.football.Match;
import com.bettorleague.server.model.football.StandingGroup;
import com.bettorleague.server.model.football.StandingStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findAllByCompetitionId(Long competitionId);
    List<Match> findAllByCompetitionIdAndStage(Long competitionId, StandingStage stage);
    List<Match> findAllByCompetitionIdAndGroup(Long competitionId, StandingGroup group);
    List<Match> findAllByCompetitionIdAndStageAndGroup(Long competitionId, StandingStage stage, StandingGroup group);


    List<Match> findAllByCompetitionIdAndMatchday(Long competitionId, Integer matchDay);
    List<Match> findAllByCompetitionIdAndMatchdayAndStage(Long competitionId, Integer matchDay, StandingStage stage);
    List<Match> findAllByCompetitionIdAndMatchdayAndGroup(Long competitionId, Integer matchDay, StandingGroup group);
    List<Match> findAllByCompetitionIdAndMatchdayAndStageAndGroup(Long competitionId, Integer matchDay, StandingStage stage, StandingGroup group);

    @Query(value = "SELECT * FROM FIXTURE u WHERE (u.HOME_TEAM_ID = ? OR u.AWAY_TEAM_ID = ?) AND u.COMPETITION_ID = ? AND u.STATUS = ?",nativeQuery = true)
    List<Match> findAllByHomeTeamIdOrAwayTeamIdAndCompetitionIdAndStatus(Long homeTeamId, Long awayTeamId, Long competitionId, String status);

}
