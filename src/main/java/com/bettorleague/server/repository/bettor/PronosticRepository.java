package com.bettorleague.server.repository.bettor;

import com.bettorleague.server.model.bettor.Pronostic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PronosticRepository extends JpaRepository<Pronostic, Long> {
    Optional<Pronostic> findByMatchIdAndPlayerId(Long matchId, Long playerId);
}
