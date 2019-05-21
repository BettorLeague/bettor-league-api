package com.bettorleague.server.repository.football;


import com.bettorleague.server.model.bettor.Contest;
import com.bettorleague.server.model.bettor.ContestType;
import com.bettorleague.server.model.football.Competition;
import com.bettorleague.server.model.football.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);
    List<Team> findAllByCompetitions(Competition competition);
    boolean existsByName(String name);
}
