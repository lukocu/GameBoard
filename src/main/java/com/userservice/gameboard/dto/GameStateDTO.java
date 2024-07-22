package com.userservice.gameboard.dto;

import java.util.List;

public class GameStateDTO {
    private GameSessionDTO gameSession;
    private List<PlayerDTO> players;
    private List<UnitDTO> units;
    private List<CommandHistoryDTO> commandHistory;

    public GameSessionDTO getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSessionDTO gameSession) {
        this.gameSession = gameSession;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public List<UnitDTO> getUnits() {
        return units;
    }

    public void setUnits(List<UnitDTO> units) {
        this.units = units;
    }

    public List<CommandHistoryDTO> getCommandHistory() {
        return commandHistory;
    }

    public void setCommandHistory(List<CommandHistoryDTO> commandHistory) {
        this.commandHistory = commandHistory;
    }
}
