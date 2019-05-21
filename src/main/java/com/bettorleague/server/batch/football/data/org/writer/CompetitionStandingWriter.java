package com.bettorleague.server.batch.football.data.org.writer;

import com.bettorleague.server.model.football.Competition;
import com.bettorleague.server.model.football.Standing;
import com.bettorleague.server.model.football.StandingTable;
import com.bettorleague.server.repository.football.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CompetitionStandingWriter implements ItemWriter<Standing> {

    private final CompetitionRepository competitionRepository;
    private final TeamRepository teamRepository;
    private final StandingRepository standingRepository;
    private final TableRepository tableRepository;

    private Optional<Competition> competitionOptional;

    public CompetitionStandingWriter(CompetitionRepository competitionRepository,
                                     TeamRepository teamRepository,
                                     StandingRepository standingRepository,
                                     TableRepository tableRepository,
                                     String competitionId) {
        this.competitionRepository = competitionRepository;
        this.standingRepository = standingRepository;
        this.teamRepository = teamRepository;
        this.tableRepository = tableRepository;
        this.competitionOptional = competitionRepository.findById(Long.valueOf(competitionId));
    }

    @Override
    public void write(List<? extends Standing> standings) {

        if (competitionOptional.isPresent()) {
            Competition competition = competitionOptional.get();

            for(Standing standing : standings){

                Optional<Standing> standingOptional = standingRepository.findByCompetitionIdAndStageAndTypeAndGroup(competition.getId(),standing.getStage(),standing.getType(),standing.getGroup());
                standingOptional.ifPresent(standingRepository::delete);

                List<StandingTable> standingTables = standing.getTable();

                standing.setCompetition(competition);
                standing.setTable(new ArrayList<>());
                standing = standingRepository.save(standing);

                for(StandingTable standingTable : standingTables){
                    standingTable.setId(null);
                    standingTable.setStanding(standing);
                    teamRepository.findById(standingTable.getTeam().getId()).ifPresent(standingTable::setTeam);
                    standingTable = tableRepository.save(standingTable);
                }

                standing.setTable(standingTables);
                standing = standingRepository.save(standing);

            }

        }else{
            log.warn("Aborted : cannot retrieve competition");
        }

    }
}
