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

    List<Match> findAllByCompetitionIdAndSeasonId(Long competitionId,Long seasonId);
    List<Match> findAllByCompetitionIdAndSeasonIdAndStage(Long competitionId,Long seasonId, StandingStage stage);
    List<Match> findAllByCompetitionIdAndSeasonIdAndGroup(Long competitionId,Long seasonId, StandingGroup group);
    List<Match> findAllByCompetitionIdAndSeasonIdAndStageAndGroup(Long competitionId,Long seasonId, StandingStage stage, StandingGroup group);


    List<Match> findAllByCompetitionIdAndSeasonIdAndMatchday(Long competitionId,Long seasonId, Integer matchDay);
    List<Match> findAllByCompetitionIdAndSeasonIdAndMatchdayAndStage(Long competitionId,Long seasonId, Integer matchDay, StandingStage stage);
    List<Match> findAllByCompetitionIdAndSeasonIdAndMatchdayAndGroup(Long competitionId,Long seasonId, Integer matchDay, StandingGroup group);
    List<Match> findAllByCompetitionIdAndSeasonIdAndMatchdayAndStageAndGroup(Long competitionId,Long seasonId, Integer matchDay, StandingStage stage, StandingGroup group);

    @Query(value = "SELECT * FROM FIXTURE u WHERE (u.HOME_TEAM_ID = ? OR u.AWAY_TEAM_ID = ?) AND u.COMPETITION_ID = ? AND u.STATUS = ?",nativeQuery = true)
    List<Match> findAllByHomeTeamIdOrAwayTeamIdAndCompetitionIdAndStatus(Long homeTeamId, Long awayTeamId, Long competitionId, String status);

}
