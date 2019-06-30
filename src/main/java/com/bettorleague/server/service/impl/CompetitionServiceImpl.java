package com.bettorleague.server.service.impl;

import com.bettorleague.server.exception.ResourceNotFoundException;
import com.bettorleague.server.model.football.*;
import com.bettorleague.server.repository.football.CompetitionRepository;
import com.bettorleague.server.repository.football.MatchRepository;
import com.bettorleague.server.repository.football.StandingRepository;
import com.bettorleague.server.repository.football.TeamRepository;
import com.bettorleague.server.service.CompetitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final MatchRepository matchRepository;
    private final StandingRepository standingRepository;
    private final TeamRepository teamRepository;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository,
                                  MatchRepository matchRepository,
                                  TeamRepository teamRepository,
                                  StandingRepository standingRepository){
        this.competitionRepository = competitionRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.standingRepository = standingRepository;
    }

    public List<Competition> getAllCompetition(){
        return this.competitionRepository.findAll();
    }

    public Optional<Competition> getCompetitionById(Long competitionId){
        return this.competitionRepository.findById(competitionId);
    }
    public List<Team> getAllTeamOfCompetition(Long competitionId){
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow( () -> new ResourceNotFoundException("Competition", "id", competitionId) );

        return this.teamRepository.findAllByCompetitions(competition);
    }
    public List<Match> getAllMatchesOfCompetition(Long competitionId, Integer matchDay, StandingStage stage, StandingGroup group){
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow( () -> new ResourceNotFoundException("Competition", "id", competitionId) );
        List<Match> matches;
        if(matchDay == null && stage == null && group == null){
            matches = this.matchRepository.findAllByCompetitionIdAndSeasonId(competitionId,competition.getCurrentSeason().getId());
        }else {
            if(matchDay != null){
                if(stage == null && group == null){
                    matches =  this.matchRepository.findAllByCompetitionIdAndSeasonIdAndMatchday(competitionId,competition.getCurrentSeason().getId(),matchDay);
                }else if (stage == null){
                    matches =  this.matchRepository.findAllByCompetitionIdAndSeasonIdAndMatchdayAndGroup(competitionId,competition.getCurrentSeason().getId(),matchDay,group);
                }else if( group == null){
                    matches =  this.matchRepository.findAllByCompetitionIdAndSeasonIdAndMatchdayAndStage(competitionId,competition.getCurrentSeason().getId(),matchDay,stage);
                }else {
                    matches =  this.matchRepository.findAllByCompetitionIdAndSeasonIdAndMatchdayAndStageAndGroup(competitionId,competition.getCurrentSeason().getId(),matchDay,stage,group);
                }
            }else {
                if(stage == null){
                    matches =  this.matchRepository.findAllByCompetitionIdAndSeasonIdAndGroup(competitionId,competition.getCurrentSeason().getId(),group);
                }else if( group == null){
                    matches =  this.matchRepository.findAllByCompetitionIdAndSeasonIdAndStage(competitionId,competition.getCurrentSeason().getId(),stage);
                }else {
                    matches =  this.matchRepository.findAllByCompetitionIdAndSeasonIdAndStageAndGroup(competitionId,competition.getCurrentSeason().getId(),stage,group);
                }
            }
        }
        Collections.sort(matches);
        return matches;
    }
    public List<Standing> getAllStandingsOfCompetition(Long competitionId, StandingType type, StandingGroup group){
        List<Standing> standings;
        if(type == null && group == null){
            standings = this.standingRepository.findAllByCompetitionId(competitionId);
        }else {
            if(type == null){
                standings =  this.standingRepository.findAllByCompetitionIdAndGroup(competitionId,group);
            }else if( group == null){
                standings = this.standingRepository.findAllByCompetitionIdAndType(competitionId,type);
            }else {
                standings = this.standingRepository.findAllByCompetitionIdAndTypeAndGroup(competitionId,type,group);
            }
        }
        Collections.sort(standings);
        return standings;
    }


}
