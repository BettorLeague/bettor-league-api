package com.bettorleague.server.rest;

import com.bettorleague.server.model.football.Match;
import com.bettorleague.server.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @RequestMapping(path = "/match", method = RequestMethod.GET)
    public ResponseEntity<Page<Match>> getAllMatch(@Valid @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                   @Valid @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                                   @Valid @RequestParam(value = "sort", required = false, defaultValue = "utcDate") String sort,
                                                   @Valid @RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") Sort.Direction sortDirection) {
        return new ResponseEntity<>(matchService.getAllMatch(PageRequest.of(page, size, Sort.by(sortDirection.equals(Sort.Direction.ASC) ? Sort.Order.asc(sort) : Sort.Order.desc(sort)))), HttpStatus.OK);
    }

    @RequestMapping(path = "/match/{matchId}", method = RequestMethod.GET)
    public ResponseEntity<Match> getMatch(@PathVariable("matchId") Long matchId) {
        return new ResponseEntity<>(matchService.getMatchById(matchId), HttpStatus.OK);
    }
}
