package com.bettorleague.server.repository.football;


import com.bettorleague.server.model.football.Standing;
import com.bettorleague.server.model.football.StandingTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface TableRepository extends JpaRepository<StandingTable, Long> {
    Set<StandingTable> findAllByStanding(Standing standing);
}
