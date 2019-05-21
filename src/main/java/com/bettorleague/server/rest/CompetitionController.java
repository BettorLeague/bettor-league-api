package com.bettorleague.server.rest;

import com.bettorleague.server.exception.ResourceNotFoundException;
import com.bettorleague.server.model.football.*;
import com.bettorleague.server.repository.football.CompetitionRepository;
import com.bettorleague.server.repository.football.StandingRepository;
import com.bettorleague.server.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class CompetitionController {

    @Autowired
    CompetitionRepository competitionRepository;

    @Autowired
    StandingRepository standingRepository;

    @Autowired
    CompetitionService competitionService;

    @RequestMapping(path = "/competition", method = RequestMethod.GET)
    public ResponseEntity<List<Competition>> getAllCompetition() {
        return new ResponseEntity<>(competitionRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(path = "/competition/{competitionId}/teams", method = RequestMethod.GET)
    public ResponseEntity<Set<Team>> getCompetitionTeam(@PathVariable("competitionId") Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition", "id", competitionId));
        return new ResponseEntity<>(competition.getTeams(),HttpStatus.OK);


    }
    @RequestMapping(path = "/competition/{competitionId}", method = RequestMethod.GET)
    public ResponseEntity<Competition> getCompetitionById(@PathVariable("competitionId") Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition", "id", competitionId));
        return new ResponseEntity<>(competition,HttpStatus.OK);
    }

    @RequestMapping(path = "/competition/{competitionId}/standings", method = RequestMethod.GET)
    public ResponseEntity<List<Standing>> getAllStandingsOfCompetition(@PathVariable("competitionId") Long competitionId,
                                                                       @RequestParam(value = "type",required = false) StandingType type,
                                                                       @RequestParam(value = "group",required = false) StandingGroup group) {
        competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition", "id", competitionId));
        return new ResponseEntity<>(this.competitionService.getAllStandingsOfCompetition(competitionId,type,group),HttpStatus.OK);
    }

    @RequestMapping(path = "/competition/{competitionId}/matches", method = RequestMethod.GET)
    public ResponseEntity<List<Match>> getAllMatchesOfCompetition(@PathVariable("competitionId") Long competitionId,
                                                                  @RequestParam(value = "matchday", required=false) Integer matchday,
                                                                  @RequestParam(value = "stage", required=false) StandingStage stage,
                                                                  @RequestParam(value = "group", required=false) StandingGroup group) {
        competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition", "id", competitionId));
        return new ResponseEntity<>(this.competitionService.getAllMatchesOfCompetition(competitionId,matchday,stage,group),HttpStatus.OK);
    }





}
