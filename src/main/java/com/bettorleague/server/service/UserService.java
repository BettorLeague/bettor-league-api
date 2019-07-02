package com.bettorleague.server.service;

import com.bettorleague.server.dto.contest.PronosticRequest;
import com.bettorleague.server.dto.contest.StatisticResponse;
import com.bettorleague.server.model.bettor.Contest;
import com.bettorleague.server.model.bettor.Message;
import com.bettorleague.server.model.bettor.Player;
import com.bettorleague.server.model.bettor.Pronostic;
import com.bettorleague.server.model.football.Team;
import com.bettorleague.server.model.security.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    List<User> getAll();
    User deleteUser(Long userId);

    List<Player> getPlayers(Long userId);
    List<Contest> getContests(Long userId);
    List<Pronostic> getPronostics(Long userId);

    List<Pronostic> getContestPronostics(Long contestId,Long userId);
    List<Message> getContestMessages(Long contestId,Long userId);
    Player getContestPlayer(Long contestId,Long userId);

    Player subscribe(Long contestId,Long userId);
    Player unSubscribe(Long contestId,Long userId);

    Team subscribeTeam(Long teamId, Long userId);
    Team unSubscribeTeam(Long teamId, Long userId);
    Set<Team> getSubscribedTeam(Long userId);

    Set<User> getFollowers(Long userId);
    Set<User> getFollowing(Long userId);
    Set<User> follow(Long userId, Long followingId);
    Set<User> unFollow(Long userId, Long followingId);

    Pronostic pronostic(Long contestId, Long userId, PronosticRequest pronosticRequest);
    StatisticResponse getStats(Long userId);
}
