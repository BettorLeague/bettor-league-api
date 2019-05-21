package com.bettorleague.server.batch.football.data.org.writer;

import com.bettorleague.server.model.football.Area;
import com.bettorleague.server.model.football.Competition;
import com.bettorleague.server.model.football.Team;
import com.bettorleague.server.repository.football.AreaRepository;
import com.bettorleague.server.repository.football.CompetitionRepository;
import com.bettorleague.server.repository.football.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CompetitionTeamWriter implements ItemWriter<Team> {

    private final CompetitionRepository competitionRepository;
    private final TeamRepository teamRepository;
    private final AreaRepository areaRepository;
    private int nexTeamIndex;
    private Optional<Competition> competitionOptional;

    public CompetitionTeamWriter(CompetitionRepository competitionRepository,
                                 TeamRepository teamRepository,
                                 AreaRepository areaRepository,
                                 String competitionId) {
        this.competitionRepository = competitionRepository;
        this.teamRepository = teamRepository;
        this.areaRepository = areaRepository;
        this.competitionOptional = competitionRepository.findById(Long.valueOf(competitionId));
    }

    @Override
    public void write(List<? extends Team> teams) {


        if (competitionOptional.isPresent()) {
            Competition competition = competitionOptional.get();

            for (Team team : teams) {

                Optional<Team> teamOptional = teamRepository.findById(team.getId());

                if (teamOptional.isPresent()) {
                    Team teamToUpdate = teamOptional.get();
                    teamToUpdate.getCompetitions().add(competition);

                    teamRepository.save(teamToUpdate);

                    competition.getTeams().add(teamToUpdate);
                    competitionRepository.save(competition);

                } else {

                    Optional<Area> areaOptional = areaRepository.findById(team.getArea().getId());

                    if (areaOptional.isPresent()) {
                        team.setArea(areaOptional.get());
                    } else {
                        team.setArea(areaRepository.save(team.getArea()));
                    }

                    team.getCompetitions().add(competition);
                    team = teamRepository.save(team);

                    competition.getTeams().add(team);
                    competitionRepository.save(competition);

                    log.info("[ " + competition.getName() + " ] [ " + team.getName() + " ] added.");
                }


            }

        } else {
            log.warn("Aborted : cannot retrieve competition");
        }


    }
}
