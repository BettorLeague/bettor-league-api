package com.bettorleague.server.batch.football.data.org.writer;

import com.bettorleague.server.model.football.*;
import com.bettorleague.server.repository.football.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bettorleague.server.model.football.StandingStage.REGULAR_SEASON;

@Slf4j
public class CompetitionInformationWriter implements Tasklet {

    private final CompetitionRepository competitionRepository;
    private final SeasonRepository seasonRepository;
    private final MatchRepository matchRepository;
    private final StandingRepository standingRepository;
    private final TeamRepository teamRepository;
    private final Optional<Competition> competitionOptional;

    public CompetitionInformationWriter(CompetitionRepository competitionRepository,
                                        SeasonRepository seasonRepository,
                                        MatchRepository matchRepository,
                                        StandingRepository standingRepository,
                                        TeamRepository teamRepository,
                                        String competitionId) {
        this.competitionRepository = competitionRepository;
        this.seasonRepository = seasonRepository;
        this.matchRepository = matchRepository;
        this.standingRepository= standingRepository;
        this.teamRepository = teamRepository;
        this.competitionOptional = competitionRepository.findById(Long.valueOf(competitionId));
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        if(competitionOptional.isPresent()){
            Competition competition = competitionOptional.get();
            List<Match> matches = matchRepository.findAllByCompetitionIdAndSeasonId(competition.getId(),competition.getCurrentSeason().getId());
            List<Standing> standings = standingRepository.findAllByCompetitionId(competition.getId());
            List<Team> teams = teamRepository.findAllByCompetitions(competition);

            for(Match match: matches){
                final StandingStage bufferStage = match.getStage();
                if(bufferStage != null && competition.getAvailableStage().stream().noneMatch(stage -> stage.equals(bufferStage))){
                    competition.getAvailableStage().add(match.getStage());
                }
            }
            for(Standing standing: standings){
                final StandingGroup bufferGroup = standing.getGroup();
                if (bufferGroup != null && competition.getAvailableGroup().stream().noneMatch(group -> group.equals(bufferGroup))) {
                    competition.getAvailableGroup().add(standing.getGroup());
                }
            }

            if (competition.getAvailableStage().contains(REGULAR_SEASON)){
                Season season = competition.getCurrentSeason();
                season.setAvailableMatchPerDay(getMatchPerDay(teams));
                season.setAvailableMatchday(getMaxMatchDay(teams));
                season = seasonRepository.save(season);

                competition.setType(Competitiontype.LEAGUE);
            }else{
                competition.setType(Competitiontype.CUP);
            }


            competition = competitionRepository.save(competition);

            if (competition.getType().equals(Competitiontype.LEAGUE)){
                Season season = competition.getCurrentSeason();
                Optional<Standing> standingOptional = competition.getStandings().stream()
                        .filter(item -> item.getType().equals(StandingType.TOTAL))
                        .findFirst();
                if(standingOptional.isPresent()){
                    Optional<StandingTable> standingTableOptional = standingOptional.get().getTable()
                            .stream()
                            .filter(item -> item.getPosition() == 1)
                            .findFirst();
                    if(standingTableOptional.isPresent()){
                        Team team = standingTableOptional.get().getTeam();
                        season.setCurrentLeader(team);
                    }
                }
                seasonRepository.save(season);
            }
        }

        return RepeatStatus.FINISHED;
    }

    private Long getMaxMatchDay(List<Team> teams){
        Number max = (teams.size() - 1) * 2;
        return max.longValue();
    }

    private Long getMatchPerDay(List<Team> teams){
        Number number = teams.size() / 2;
        return number.longValue();
    }
}
