package com.bettorleague.server.repository.football;


import com.bettorleague.server.model.football.Standing;
import com.bettorleague.server.model.football.StandingGroup;
import com.bettorleague.server.model.football.StandingStage;
import com.bettorleague.server.model.football.StandingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StandingRepository extends JpaRepository<Standing, Long> {

    boolean existsByCompetitionId(Long competitionId);

    List<Standing> findAllByCompetitionId(Long competitionId);

    List<Standing> findAllByCompetitionIdAndType(Long competitionId, StandingType type);
    List<Standing> findAllByCompetitionIdAndTypeAndStage(Long competitionId, StandingType type, StandingStage stage);
    List<Standing> findAllByCompetitionIdAndTypeAndGroup(Long competitionId, StandingType type, StandingGroup group);
    List<Standing> findAllByCompetitionIdAndTypeAndStageAndGroup(Long competitionId, StandingType type, StandingStage stage, StandingGroup group);


    List<Standing> findAllByCompetitionIdAndStage(Long competitionId, StandingStage stage);
    List<Standing> findAllByCompetitionIdAndGroup(Long competitionId, StandingGroup group);
    List<Standing> findAllByCompetitionIdAndStageAndGroup(Long competitionId, StandingStage stage, StandingGroup group);

    void deleteAllByCompetitionId(Long competitionId);

    Optional<Standing> findByCompetitionIdAndStageAndTypeAndGroup(Long competitionId,StandingStage standingStage,StandingType type,StandingGroup group);
}
