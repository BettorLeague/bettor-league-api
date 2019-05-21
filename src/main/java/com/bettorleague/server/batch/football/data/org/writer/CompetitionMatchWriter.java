package com.bettorleague.server.batch.football.data.org.writer;

import com.bettorleague.server.model.football.*;
import com.bettorleague.server.repository.football.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CompetitionMatchWriter implements ItemWriter<Match> {

    private final CompetitionRepository competitionRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final ScoreRepository scoreRepository;

    private Optional<Competition> competitionOptional;

    public CompetitionMatchWriter(CompetitionRepository competitionRepository,
                                  MatchRepository matchRepository,
                                  TeamRepository teamRepository,
                                  SeasonRepository seasonRepository,
                                  ScoreRepository scoreRepository,
                                  String competitionId) {
        this.competitionRepository = competitionRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.seasonRepository = seasonRepository;
        this.scoreRepository = scoreRepository;
        this.competitionOptional = competitionRepository.findById(Long.valueOf(competitionId));
    }

    @Override
    public void write(List<? extends Match> matches) {


        if (competitionOptional.isPresent()) {
            Competition competition = competitionOptional.get();


            for (Match match : matches) {
                Optional<Match> matchOptional = matchRepository.findById(match.getId());

                if (matchOptional.isPresent()) {
                    Match matchToUpdate = matchOptional.get();
                    Long scoreId = matchToUpdate.getScore().getId();
                    matchToUpdate.setScore(match.getScore());
                    matchToUpdate.getScore().setId(scoreId);
                    matchToUpdate.getScore().setMatch(matchToUpdate);
                    matchToUpdate.setLastUpdated(match.getLastUpdated());
                    matchToUpdate.setStatus(match.getStatus());
                    match = matchRepository.save(matchToUpdate);
                    //log.info("[ " + competition.getName() + " ] "+match.getHomeTeam().getName()+" vs "+match.getAwayTeam().getName()+" updated.");
                }
                else {
                    match.setCompetition(competition);
                    seasonRepository.findById(match.getSeason().getId()).ifPresent(match::setSeason);
                    teamRepository.findById(match.getHomeTeam().getId()).ifPresent(match::setHomeTeam);
                    teamRepository.findById(match.getAwayTeam().getId()).ifPresent(match::setAwayTeam);
                    match = matchRepository.save(match);
                    match.getScore().setMatch(match);
                    match = matchRepository.save(match);
                    log.info("[ " + competition.getName() + " ] "+match.getHomeTeam().getName()+" vs "+match.getAwayTeam().getName()+" added.");
                }

            }

        } else {
            log.warn("Aborted : cannot retrieve competition");
        }


    }



}
