package com.bettorleague.server.repository.football;


import com.bettorleague.server.model.football.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    Area findByName(String name);
    boolean existsByName(String name);
}
