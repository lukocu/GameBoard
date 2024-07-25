package com.userservice.gameboard.service;

import com.userservice.gameboard.configuration.BoardConfiguration;
import com.userservice.gameboard.dto.BoardDTO;
import com.userservice.gameboard.dto.mappers.BoardMapper;
import com.userservice.gameboard.model.*;
import com.userservice.gameboard.repository.BoardRepository;
import com.userservice.gameboard.repository.CommandHistoryRepository;
import com.userservice.gameboard.repository.PlayerRepository;
import com.userservice.gameboard.repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BattleService {

    private final BoardRepository boardRepository;
    private final GameSessionService gameSessionService;
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;
    private final BoardService boardService;
    private final UnitRepository unitRepository;
    private final CommandHistoryRepository commandHistoryRepository;
    private final BoardConfiguration boardConfiguration;
    private Board board;
    private GameSession currentGameSession;
    private BoardMapper boardMapper;

    private Set<String> occupiedPositions = new HashSet<>();
    Random random = new Random();
    @Autowired
    public BattleService(BoardRepository boardRepository, BoardConfiguration boardConfiguration, PlayerService playerService, BoardService boardService,
                         UnitRepository unitRepository, CommandHistoryRepository commandHistoryRepository,PlayerRepository playerRepository, GameSessionService gameSessionService, BoardMapper boardMapper) {
        this.boardRepository = boardRepository;
        this.boardConfiguration = boardConfiguration;
        this.playerService = playerService;
        this.boardService = boardService;
        this.unitRepository = unitRepository;
        this.commandHistoryRepository = commandHistoryRepository;
        this.playerRepository = playerRepository;
        this.gameSessionService = gameSessionService;
        this.boardMapper = boardMapper;
    }

    @Transactional
    public BoardDTO createBoard(Player player1, Player player2) {

        board = new Board(boardConfiguration.getWidth(), boardConfiguration.getHeight());

        currentGameSession = gameSessionService.startNewGame(board);

        player1.setGameSession(currentGameSession);
        player2.setGameSession(currentGameSession);
        playerService.createPlayers(player1, player2);
        board.setGameSession(currentGameSession);


        placeUnitsRandomly(board, player1, boardConfiguration.getArcherCount(), "Archer",currentGameSession);
        placeUnitsRandomly(board, player1, boardConfiguration.getCannonCount(), "Cannon",currentGameSession);
        placeUnitsRandomly(board, player1, boardConfiguration.getTransportCount(), "Transport",currentGameSession);

        placeUnitsRandomly(board, player2, boardConfiguration.getArcherCount(), "Archer",currentGameSession);
        placeUnitsRandomly(board, player2, boardConfiguration.getCannonCount(), "Cannon",currentGameSession);
        placeUnitsRandomly(board, player2, boardConfiguration.getTransportCount(), "Transport",currentGameSession);


        Board savedBoard = boardRepository.save(board);

        return boardMapper.toDTO(savedBoard);
    }

    public Board getBoard() {
        return board;
    }

    @Transactional
    public boolean moveUnit(String playerColor, Long unitId, int newX, int newY) {
        Board board = boardRepository.findByGameSessionId(currentGameSession.getId());
        if (board == null) {
            throw new IllegalStateException("No board found for the current game session");
        }
        boolean result = boardService.moveUnit(board, playerColor, unitId, newX, newY);
        if (result) {
            commandHistoryRepository.save(new CommandHistory("Colour player " + playerColor + " move " + unitId + " to " + newX + "," + newY, currentGameSession));
        }
        return result;
    }

    @Transactional
    public String shoot(String playerColor, Long unitId, int targetX, int targetY) {
        String result = boardService.shoot(board, playerColor, unitId, targetX, targetY);
        commandHistoryRepository.save(new CommandHistory("Colour player " + playerColor + " shoot " + unitId + " at " + targetX + "," + targetY, currentGameSession));
        return result;
    }

    public void endGame() {
        gameSessionService.endGame(currentGameSession.getId());
    }

    private void placeUnitsRandomly(Board board, Player player, int count, String unitType, GameSession currentGameSession) {

        for (int i = 0; i < count; i++) {
            int x, y;
            String position;

            do {
                x = random.nextInt(board.getWidth());
                y = random.nextInt(board.getHeight());
                position = x + "," + y;
            } while (occupiedPositions.contains(position));

            occupiedPositions.add(position);

            Unit unit;
            switch (unitType) {
                case "Archer":
                    unit = new Archer("Archer", x, y,player, currentGameSession);
                    break;
                case "Cannon":
                    unit = new Cannon("Cannon", x, y,player, currentGameSession);
                    break;
                case "Transport":
                    unit = new Transport("Transport", x, y,player, currentGameSession);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown unit type: " + unitType);
            }

            unit.setGameSession(currentGameSession);

            board.addUnit(unit);
            unitRepository.save(unit);
        }
    }

    @Transactional
    public void startNewGame(Player player1, Player player2) {
        if (currentGameSession != null) {
            endGame();
        }
        createBoard(player1, player2);
    }

    public GameSession getGameSession(Long GamgeSessionId) {
        return gameSessionService.getGameSession(GamgeSessionId);
    }

    public void setCurrentGameSession(GameSession currentGameSession) {
        this.currentGameSession = currentGameSession;
    }

    public List<CommandHistory> getCommandHistory(Long gameSessionId) {
        return commandHistoryRepository.findByGameSessionId(gameSessionId);
    }

    public List<Unit> getUnits(Long gameSessionId) {
        return unitRepository.findByGameSessionId(gameSessionId);
    }

    public List<Player> getPlayers(Long gameSessionId) {
        return playerRepository.getPlayersByGameSessionId(gameSessionId);
    }


    @Transactional
    public String moveUnitRandomly(String playerColor, Long unitId) {
        Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new IllegalArgumentException("Unit not found"));

        if (unit instanceof Cannon) {
            return "Cannon cannot move";
        }

        int boardWidth = unit.getGameSession().getBoard().getWidth();
        int boardHeight = unit.getGameSession().getBoard().getHeight();

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int maxDistance = 1;

        if (unit instanceof Transport) {
            maxDistance = random.nextInt(3) + 1;
        }


        List<int[]> shuffledDirections = new ArrayList<>(List.of(directions));
        Collections.shuffle(shuffledDirections);

        for (int[] direction : shuffledDirections) {
            int distance = random.nextInt(maxDistance) + 1;
            int newX = unit.getX() + direction[0] * distance;
            int newY = unit.getY() + direction[1] * distance;

            if (newX >= 0 && newX < boardWidth && newY >= 0 && newY < boardHeight) {
                boolean result = boardService.moveUnit(unit.getGameSession().getBoard(), playerColor, unitId, newX, newY);
                if (result) {
                    return "Unit " + unit.getId() + " moved to (" + newX + ", " + newY + ")";
                }
            }
        }

        return "Unit " + unit.getId() + " could not move";
    }

}