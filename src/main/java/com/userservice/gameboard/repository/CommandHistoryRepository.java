package com.userservice.gameboard.repository;

import com.userservice.gameboard.model.CommandHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandHistoryRepository extends JpaRepository<CommandHistory, Long > {
    CommandHistory save(CommandHistory commandHistory);

    List<CommandHistory> findByGameSessionId(Long gameSessionId);

}
