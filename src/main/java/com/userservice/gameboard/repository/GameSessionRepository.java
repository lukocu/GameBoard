package com.userservice.gameboard.repository;

import com.userservice.gameboard.model.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession,Long> {
    GameSession save(GameSession gameSession);

    Optional<GameSession> findById(Long gameId);
}
