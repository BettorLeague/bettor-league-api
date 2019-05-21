package com.bettorleague.server.repository.bettor;

import com.bettorleague.server.model.bettor.Contest;
import com.bettorleague.server.model.bettor.ContestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findAllByType(ContestType type);

    Optional<Contest> findByTypeAndCompetitionId(ContestType type, Long competitionId);

    Page<Contest> findByType(ContestType type, Pageable pageable);
}
