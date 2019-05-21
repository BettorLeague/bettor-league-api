package com.bettorleague.server.repository.bettor;



import com.bettorleague.server.model.bettor.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findAllByContestId(Long contestId);
    List<Player> findAllByContestIdOrderByPointsDesc(Long contestId);
    List<Player> findAllByUserId(Long userId);

    Player findByUserIdAndContestId(Long userId, Long contestId);
    boolean existsByUserIdAndContestId(Long userId, Long contestId);
}
