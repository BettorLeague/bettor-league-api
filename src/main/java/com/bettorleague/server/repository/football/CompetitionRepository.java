package com.bettorleague.server.repository.football;


import com.bettorleague.server.model.football.Competition;
import com.bettorleague.server.model.football.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    boolean existsByName(String name);
    boolean existsByCode(String code);

    Competition findByName(String name);
    Competition findByCode(String code);
    Competition findByCurrentSeason(Season season);
}
