package com.userservice.gameboard.dto.mappers;

import com.userservice.gameboard.dto.BoardDTO;
import com.userservice.gameboard.model.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BoardMapper {

    @Autowired
    private UnitMapper unitMapper;

    public BoardDTO toDTO(Board board) {
        BoardDTO dto = new BoardDTO();
        dto.setId(board.getId());
        dto.setWidth(board.getWidth());
        dto.setHeight(board.getHeight());
        dto.setUnits(board.getUnitsById().values().stream().map(unitMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }

}
