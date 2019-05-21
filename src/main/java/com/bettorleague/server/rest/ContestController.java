package com.bettorleague.server.rest;

import com.bettorleague.server.dto.contest.ContestRequest;
import com.bettorleague.server.model.bettor.*;
import com.bettorleague.server.security.model.CurrentUser;
import com.bettorleague.server.security.model.UserPrincipal;
import com.bettorleague.server.service.ContestService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @RequestMapping(path = "/contest", method = RequestMethod.GET)
    public ResponseEntity<Page<Contest>> getAllContest(@Valid @RequestParam(value = "type", required = false) ContestType type,
                                                       @Valid @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                       @Valid @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                                       @Valid @RequestParam(value = "sort", required = false, defaultValue = "totalPlayer") String sort,
                                                       @Valid @RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") Sort.Direction sortDirection ) {
        return new ResponseEntity<>(contestService.getContestPage(type, PageRequest.of( page, size, Sort.by(sortDirection.equals(Sort.Direction.ASC) ? Sort.Order.asc(sort) : Sort.Order.desc(sort) , Sort.Order.asc("caption") ) ) ),HttpStatus.OK);
    }

    @RequestMapping(path = "/contest", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Contest> addContest(@Valid ContestRequest contestRequest,
                                              @ApiIgnore
                                              @ApiParam(hidden = true)
                                              @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(contestService.addContest(contestRequest,userPrincipal.getId()),HttpStatus.CREATED);
    }

    @RequestMapping(path = "/contest/{contestId}", method = RequestMethod.GET)
    public ResponseEntity<Contest> getContestById(@PathVariable("contestId") Long contestId) {
        return new ResponseEntity<>(contestService.getContestById(contestId),HttpStatus.OK);
    }

    @RequestMapping(path = "/contest/{contestId}", method = RequestMethod.DELETE)
    public ResponseEntity<Contest> deleteContest(@PathVariable("contestId") Long contestId) {
        return new ResponseEntity<>(contestService.deleteContest(contestId),HttpStatus.OK);
    }

    @RequestMapping(path = "/contest/{contestId}/players", method = RequestMethod.GET)
    public ResponseEntity<Set<Player>> getPlayersByContestId(@PathVariable("contestId") Long contestId) {
        return new ResponseEntity<>(contestService.getPlayer(contestId),HttpStatus.OK);
    }


    @RequestMapping(path = "/contest/{contestId}/standings", method = RequestMethod.GET)
    public ResponseEntity<List<StandingPlayer>> getStandingsContest(@PathVariable("contestId") Long contestId) {
        return new ResponseEntity<>(contestService.getStanding(contestId),HttpStatus.OK);
    }

    @RequestMapping(path = "/contest/{contestId}/messages", method = RequestMethod.GET)
    public ResponseEntity<Set<Message>> getMessagesByContestId(@PathVariable("contestId") Long contestId) {
        return new ResponseEntity<>(contestService.getMessage(contestId),HttpStatus.OK);
    }


}
