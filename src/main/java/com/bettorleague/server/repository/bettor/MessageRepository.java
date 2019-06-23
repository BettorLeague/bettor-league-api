package com.bettorleague.server.repository.bettor;

import com.bettorleague.server.model.bettor.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByContestId(Long contestId);
    List<Message> findAllByContestIdAndPlayer_Id(Long contestId, Long playerId);
    Page<Message> findAllByContestId(Long contestId, Pageable pageable);
}
