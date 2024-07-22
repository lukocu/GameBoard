package com.userservice.gameboard.dto.mappers;

import com.userservice.gameboard.dto.UnitDTO;
import com.userservice.gameboard.model.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnitMapper {

    @Autowired
    private PlayerMapper playerMapper;

    public UnitDTO toDTO(Unit unit) {
        UnitDTO unitDTO = new UnitDTO.Builder()
                .id(unit.getId())
                .type(unit.getType())
                .x(unit.getX())
                .y(unit.getY())
                .destroyed(unit.isDestroyed())
                .player(playerMapper.toDTO(unit.getPlayer()))
                .build();
        return unitDTO;
    }


}
