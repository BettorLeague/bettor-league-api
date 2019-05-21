package com.bettorleague.server.rest;

import com.bettorleague.server.dto.contest.PronosticRequest;
import com.bettorleague.server.model.bettor.Contest;
import com.bettorleague.server.model.bettor.Message;
import com.bettorleague.server.model.bettor.Player;
import com.bettorleague.server.model.bettor.Pronostic;
import com.bettorleague.server.model.football.Team;
import com.bettorleague.server.model.security.User;
import com.bettorleague.server.security.model.CurrentUser;
import com.bettorleague.server.security.model.UserPrincipal;
import com.bettorleague.server.service.UserService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(path = "/user", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<User> deleteAccount(@ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.deleteUser(userPrincipal.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/players", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Player>> getPlayers(@ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.getPlayers(userPrincipal.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/pronostics", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Pronostic>> getPronostics(@ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.getPronostics(userPrincipal.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/contests", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Contest>> getContests(@ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.getContests(userPrincipal.getId()), HttpStatus.OK);
    }



    @RequestMapping(path = "/user/contest/{contestId}/pronostic", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Pronostic> pronostic(@PathVariable("contestId") Long contestId,
                                               @Valid PronosticRequest pronosticRequest,
                                               @ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.pronostic(contestId, userPrincipal.getId(), pronosticRequest), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/contest/{contestId}/pronostic", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Pronostic>> getContestPronostics(@PathVariable("contestId") Long contestId,
                                               @ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.getContestPronostics(contestId, userPrincipal.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/contest/{contestId}/message", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Message>> getContestMessages(@PathVariable("contestId") Long contestId,
                                                     @ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.getContestMessages(contestId, userPrincipal.getId()), HttpStatus.OK);
    }


    @RequestMapping(path = "/user/contest/{contestId}/player", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Player> getContestPlayer(@PathVariable("contestId") Long contestId,
                                            @ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.getContestPlayer(contestId, userPrincipal.getId()), HttpStatus.OK);
    }


    @RequestMapping(path = "/user/contest/{contestId}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Player> subscribe(@PathVariable("contestId") Long contestId,
                                            @ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.subscribe(contestId, userPrincipal.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/contest/{contestId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Player> unSubscribe(@PathVariable("contestId") Long contestId,
                                              @ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.unSubscribe(contestId, userPrincipal.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/team}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Set<Team>> getSubscribedTeam(@ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.getSubscribedTeam(userPrincipal.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/team/{teamId}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Team> subscribeTeam(@PathVariable("teamId") Long teamId,
                                          @ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.subscribeTeam(teamId, userPrincipal.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/team/{teamId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Team> unSubscribeTeam(@PathVariable("teamId") Long teamId,
                                              @ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.unSubscribeTeam(teamId, userPrincipal.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/following", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Set<User>> getFollowing(@ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.getFollowing(userPrincipal.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/following/{userId}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Set<User>> follow(@ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal,
                                            @PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.follow(userPrincipal.getId(),userId), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/following/{userId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Set<User>> unFollow(@ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal,
                                              @PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.unFollow(userPrincipal.getId(),userId), HttpStatus.OK);
    }

    @RequestMapping(path = "/user/followers", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Set<User>> getFollowers(@ApiIgnore @ApiParam(hidden = true) @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.getFollowers(userPrincipal.getId()), HttpStatus.OK);
    }

}
