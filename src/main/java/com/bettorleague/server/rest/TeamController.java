package com.bettorleague.server.rest;

import com.bettorleague.server.model.football.Team;
import com.bettorleague.server.repository.football.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Validated
public class TeamController {

    @Autowired
    TeamRepository teamRepository;

    @RequestMapping(path = "/team", method = RequestMethod.GET)
    public ResponseEntity<Page<Team>> getAllTeam(@Valid @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                 @Valid @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                                 @Valid @RequestParam(value = "sort", required = false, defaultValue = "totalUser") String sort,
                                                 @Valid @RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") Sort.Direction sortDirection ) {
        return new ResponseEntity<>(teamRepository.findAll(PageRequest.of( page, size, Sort.by(sortDirection.equals(Sort.Direction.ASC) ? Sort.Order.asc(sort) : Sort.Order.desc(sort) , Sort.Order.asc("name") ) )), HttpStatus.OK);
    }


}
