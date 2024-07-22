package com.userservice.gameboard.dto.mappers;

import com.userservice.gameboard.dto.GameSessionDTO;
import com.userservice.gameboard.model.GameSession;
import org.springframework.stereotype.Component;

@Component
public class GameSessionMapper { ;

    public GameSessionDTO toDTO(GameSession gameSession) {
        GameSessionDTO dto = new GameSessionDTO();
        dto.setId(gameSession.getId());
        dto.setStartTime(gameSession.getStartTime());
        dto.setEndTime(gameSession.getEndTime());

        return dto;
    }


}
