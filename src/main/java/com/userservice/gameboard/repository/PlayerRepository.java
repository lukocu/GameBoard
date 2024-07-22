package com.userservice.gameboard.repository;

import com.userservice.gameboard.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> getPlayersByGameSessionId(Long gameSessionId);

    List<Player> findByColorAndGameSessionId(String color, Long id);
}
