package com.bettorleague.server.service.impl;

import com.bettorleague.server.dto.contest.PronosticRequest;
import com.bettorleague.server.dto.contest.StatisticResponse;
import com.bettorleague.server.dto.contest.UserStatistic;
import com.bettorleague.server.exception.BadRequestException;
import com.bettorleague.server.exception.ResourceNotFoundException;
import com.bettorleague.server.model.bettor.Contest;
import com.bettorleague.server.model.bettor.Message;
import com.bettorleague.server.model.bettor.Player;
import com.bettorleague.server.model.bettor.Pronostic;
import com.bettorleague.server.model.football.Team;
import com.bettorleague.server.model.security.User;
import com.bettorleague.server.repository.bettor.ContestRepository;
import com.bettorleague.server.repository.bettor.PronosticRepository;
import com.bettorleague.server.repository.football.TeamRepository;
import com.bettorleague.server.repository.security.UserRepository;
import com.bettorleague.server.security.model.UserPrincipal;
import com.bettorleague.server.service.PlayerService;
import com.bettorleague.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContestRepository contestRepository;

    @Autowired
    PlayerService playerService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PronosticRepository pronosticRepository;

    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email));
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", id));
        return UserPrincipal.create(user);
    }


    public User deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        userRepository.delete(user);
        return user;
    }

    public List<Player> getPlayers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        return new ArrayList<>(user.getPlayers());
    }


    public List<Contest> getContests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        return user.getPlayers().stream()
                .map(Player::getContest)
                .collect(Collectors.toList());
    }

    public List<Pronostic> getPronostics(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        return pronosticRepository.findAll().stream()
                .filter(pronostic -> pronostic.getPlayer().getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public Player subscribe(Long contestId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Contest", "id", contestId));

        return playerService.create(contest.getId(), user.getId());
    }

    public Player unSubscribe(Long contestId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));

        contestRepository.findById(contestId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Contest", "id", contestId));

        Player player = user.getPlayers().stream()
                .filter(item -> item.getContest().getId().equals(contestId))
                .findAny()
                .orElseThrow(() -> new BadRequestException(user.getName() + " didnt play in this contest"));

        return playerService.delete(player.getId());
    }

    private Player getPlayer(Long contestId, Long userId) {
        contestRepository.findById(contestId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Contest", "id", contestId));

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));

        return user.getPlayers().stream()
                .filter(player -> player.getContest().getId().equals(contestId))
                .findAny()
                .orElseThrow(() -> new BadRequestException(user.getName() + " didnt play in this contest"));

    }

    public Pronostic pronostic(Long contestId, Long userId, PronosticRequest pronosticRequest) {
        Player player = getPlayer(contestId, userId);
        return playerService.savePronostic(player.getId(), pronosticRequest);
    }

    public List<Pronostic> getContestPronostics(Long contestId, Long userId) {
        Player player = getPlayer(contestId, userId);
        return playerService.getPronostics(player.getId());
    }

    public List<Message> getContestMessages(Long contestId, Long userId) {
        Player player = getPlayer(contestId, userId);
        return playerService.getMessages(player.getId());
    }

    public Player getContestPlayer(Long contestId, Long userId) {
        return getPlayer(contestId, userId);
    }


    public Team subscribeTeam(Long teamId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Team", "id", teamId));

        user.getTeams().add(team);
        userRepository.save(user);
        team.getUsers().add(user);
        team = teamRepository.save(team);
        return team;
    }

    public Team unSubscribeTeam(Long teamId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Team", "id", teamId));

        team.getUsers().remove(user);
        user.getTeams().remove(team);
        userRepository.save(user);
        team = teamRepository.save(team);
        return team;
    }


    public Set<Team> getSubscribedTeam(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));

        return user.getTeams();
    }


    public Set<User> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        return user.getFollowers();
    }

    public Set<User> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        return user.getFollowing();
    }

    public Set<User> follow(Long userId, Long followingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));

        User following = userRepository.findById(followingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", followingId));

        user.getFollowing().add(following);
        following.getFollowers().add(user);
        userRepository.save(user);
        userRepository.save(following);
        return user.getFollowing();
    }

    public Set<User> unFollow(Long userId, Long followingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));

        User following = userRepository.findById(followingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", followingId));

        if (!following.getFollowers().contains(user)) {
            throw new BadRequestException("User " + user.getName() + " dont follow " + following.getName());
        }

        user.getFollowing().remove(following);
        following.getFollowers().remove(user);

        userRepository.save(user);
        userRepository.save(following);

        return user.getFollowing();
    }

    public StatisticResponse getStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", userId));
        List<Pronostic> pronostics = this.getPronostics(user.getId());

        StatisticResponse response = new StatisticResponse();

        for (Pronostic pronostic : pronostics){
            Calendar cal = Calendar.getInstance();
            cal.setTime(pronostic.getDate());
            Integer year = cal.get(Calendar.YEAR);
            Integer month = cal.get(Calendar.MONTH);

            UserStatistic userStatistic = new UserStatistic();

            if(response.getStats().containsKey(year)){
                userStatistic = response.getStats().get(year);
            }

            if (pronostic.getMatch().getStatus().equals("FINISHED") && pronostic.getMatch().getScore().getWinner().equals(pronostic.getResult())) {
                userStatistic.getGoodPronostics().add(month,userStatistic.getGoodPronostics().get(month)+1);
            }
            userStatistic.getPronostics().add(month,userStatistic.getPronostics().get(month)+1);
            userStatistic.getContests().add(pronostic.getPlayer().getContest());
            response.getStats().put(year,userStatistic);
        }

        return response;
    }

    private String getKeyByMonth(Integer month) {
        switch (month) {
            case 0:
                return "JAN";
            case 1:
                return "FEB";
            case 2:
                return "MAR";
            case 3:
                return "APR";
            case 4:
                return "MAY";
            case 5:
                return "JUN";
            case 6:
                return "JUL";
            case 7:
                return "AUG";
            case 8:
                return "SEP";
            case 9:
                return "OCT";
            case 10:
                return "NOV";
            case 11:
                return "DEC";
            default:
                return null;
        }
    }


}
