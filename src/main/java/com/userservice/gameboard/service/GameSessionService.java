package com.userservice.gameboard.service;

import com.userservice.gameboard.model.Board;
import com.userservice.gameboard.model.GameSession;
import com.userservice.gameboard.repository.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class GameSessionService {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    public GameSession startNewGame(Board board) {
        return gameSessionRepository.save(new GameSession(board));
    }
    public GameSession getGameSession(Long gameSession) {
        return gameSessionRepository.findById(gameSession).orElse(null);
    }


    public void endGame(Long gameId) {
        GameSession gameSession = gameSessionRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game session not found"));
        gameSession.setEndTime(new Date());
        gameSessionRepository.save(gameSession);
    }
}
