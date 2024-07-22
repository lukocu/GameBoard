package com.userservice.gameboard.dto.mappers;

import com.userservice.gameboard.dto.CommandHistoryDTO;
import com.userservice.gameboard.model.CommandHistory;
import org.springframework.stereotype.Component;

@Component
public class CommandHistoryMapper {

    public CommandHistoryDTO toDTO(CommandHistory commandHistory) {
        CommandHistoryDTO dto = new CommandHistoryDTO();
        dto.setId(commandHistory.getId());
        dto.setCommand(commandHistory.getCommand());
        dto.setTimestamp(commandHistory.getTimestamp());
        return dto;
    }
}
