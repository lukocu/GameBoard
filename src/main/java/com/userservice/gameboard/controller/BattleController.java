package com.userservice.gameboard.controller;

import com.userservice.gameboard.controller.request.MoveRequest;
import com.userservice.gameboard.controller.request.ShootRequest;
import com.userservice.gameboard.dto.BoardDTO;
import com.userservice.gameboard.dto.GameStateDTO;
import com.userservice.gameboard.dto.mappers.CommandHistoryMapper;
import com.userservice.gameboard.dto.mappers.GameSessionMapper;
import com.userservice.gameboard.dto.mappers.PlayerMapper;
import com.userservice.gameboard.dto.mappers.UnitMapper;
import com.userservice.gameboard.model.Board;
import com.userservice.gameboard.model.Player;
import com.userservice.gameboard.model.Unit;
import com.userservice.gameboard.service.BattleService;
import com.userservice.gameboard.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/battle")
public class BattleController {

    @Autowired
    private BattleService battleService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private GameSessionMapper gameSessionMapper;
    @Autowired
    private UnitMapper unitMapper;
    @Autowired
    private CommandHistoryMapper commandHistoryMapper;


    @GetMapping("/board")
    public Board getBoard() {
        return battleService.getBoard();
    }

    @PostMapping("/start")
    public BoardDTO startBattle(@RequestBody Player[] players) {
        if (players.length != 2) {
            throw new IllegalArgumentException("Two players are required to start the game");
        }

        if (!isValidPlayers(players)) {
            throw new IllegalArgumentException("One player must be black and the other must be white");
        }

        Player player1 = playerMapper.toEntity(players[0]);
        Player player2 = playerMapper.toEntity(players[1]);

        return battleService.createBoard(player1,player2);
    }

    @PostMapping("/move")
    public String moveUnit(@RequestBody MoveRequest moveRequest) {
        if (!playerService.isValidPlayer(moveRequest.getPlayerColor())) {
            return "Invalid player color";
        }
        boolean result = battleService.moveUnit(moveRequest.getPlayerColor(), moveRequest.getUnitId(), moveRequest.getNewX(), moveRequest.getNewY());
        return result ? "Move successful" : "Move failed";
    }

    @PostMapping("/shoot")
    public String shoot(@RequestBody ShootRequest shootRequest) {
        if (!playerService.isValidPlayer(shootRequest.getPlayerColor())) {
            return "Invalid player color";
        }
        return battleService.shoot(shootRequest.getPlayerColor(), shootRequest.getUnitId(), shootRequest.getTargetX(), shootRequest.getTargetY());
    }


    @PostMapping("/startNewGame")
    public String startNewGame(@RequestBody Player[] players) {
        if (players.length != 2) {
            throw new IllegalArgumentException("Two players are required to start the game");
        }

        battleService.startNewGame(players[0], players[1]);
        return "New game started";
    }


    @GetMapping("/state")
    public GameStateDTO getGameState(@RequestParam Long gameSessionId) {
        Map<String, Object> gameState = new HashMap<>();

        gameState.put("players", battleService.getPlayers(gameSessionId));
        gameState.put("units", battleService.getUnits(gameSessionId));
        gameState.put("commandHistory", battleService.getCommandHistory(gameSessionId));

        GameStateDTO gameStateDTO = new GameStateDTO();

        gameStateDTO.setGameSession(gameSessionMapper.toDTO(battleService.getGameSession(gameSessionId)));
        gameStateDTO.setPlayers(battleService.getPlayers(gameSessionId).stream()
                .map(playerMapper::toDTO)
                .collect(Collectors.toList()));
        gameStateDTO.setUnits(battleService.getUnits(gameSessionId).stream()
                .map(unitMapper::toDTO)
                .collect(Collectors.toList()));
        gameStateDTO.setCommandHistory(battleService.getCommandHistory(gameSessionId).stream()
                .map(commandHistoryMapper::toDTO)
                .collect(Collectors.toList()));

        return gameStateDTO;
    }
    @GetMapping("/units")
    public List<Unit> getUnits(@RequestParam Long gameSessionId) {
        return battleService.getUnits(gameSessionId);
    }


    @PostMapping("/moveRandomly")
    public String moveUnitRandomly(@RequestParam String playerColor, @RequestParam Long unitId) {
        return battleService.moveUnitRandomly(playerColor, unitId);
    }

    private boolean isValidPlayers(Player[] players) {
        return (players[0].getColor().equalsIgnoreCase("white") && players[1].getColor().equalsIgnoreCase("black")) ||
                (players[0].getColor().equalsIgnoreCase("black") && players[1].getColor().equalsIgnoreCase("white"));
    }
}
