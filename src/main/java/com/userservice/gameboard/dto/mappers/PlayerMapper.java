package com.userservice.gameboard.dto.mappers;

import com.userservice.gameboard.dto.PlayerDTO;
import com.userservice.gameboard.model.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerDTO toDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setColor(player.getColor());
        return dto;
    }

    public Player toEntity(Player dto) {
        Player player = new Player();
        player.setId(dto.getId());
        player.setName(dto.getName());
        player.setColor(dto.getColor());
        return player;
    }
}
