package com.userservice.gameboard.service;

import com.userservice.gameboard.model.*;
import com.userservice.gameboard.repository.CommandHistoryRepository;
import com.userservice.gameboard.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private CommandHistoryRepository commandHistoryRepository;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private BoardService boardService;

    private Board board;
    private Player player;
    private GameSession gameSession;
    private Unit archer;
    private Unit cannon;
    private Unit transport;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        gameSession = new GameSession();
        gameSession.setId(1L);

        board = new Board(16, 16);
        board.setGameSession(gameSession);

        player = new Player("Player1", "white", gameSession);

        archer = new Archer("Archer", 0, 0, player, gameSession);
        cannon = new Cannon("Cannon", 1, 1, player, gameSession);
        transport = new Transport("Transport", 2, 2, player, gameSession);

        setUnitId(archer, 1L);
        setUnitId(cannon, 2L);
        setUnitId(transport, 3L);

        board.addUnit(archer);
        board.addUnit(cannon);
        board.addUnit(transport);
    }

    private void setUnitId(Unit unit, Long id) {
        try {
            Field idField = Unit.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(unit, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "Archer, 0, 0, 0, 1, true",
            "Archer, 0, 0, 1, 0, true",
            "Transport, 2, 2, 5, 2, true",
            "Transport, 2, 2, 2, 5, true"
    })
    void testMoveUnitSuccess(String unitType, int startX, int startY, int newX, int newY, boolean expectedResult) {
        Unit unitToMove;
        switch (unitType) {
            case "Archer":
                unitToMove = archer;
                break;
            case "Cannon":
                unitToMove = cannon;
                break;
            case "Transport":
                unitToMove = transport;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit type");
        }

        unitToMove.setX(startX);
        unitToMove.setY(startY);

        when(playerService.getPlayerByColorAndSession(anyString(), any(GameSession.class))).thenReturn(player);
        when(playerService.canExecuteCommand(any(Player.class))).thenReturn(true);
        when(unitRepository.save(any(Unit.class))).thenReturn(unitToMove);

        boolean result = boardService.moveUnit(board, "white", unitToMove.getId(), newX, newY);

        assertEquals(expectedResult, result);

            assertEquals(newX, unitToMove.getX());
            assertEquals(newY, unitToMove.getY());

    }

    @ParameterizedTest
    @CsvSource({
            "Archer, 0, 0, 1, 1, false",
            "Cannon, 1, 1, 2, 2, false",
            "Transport, 2, 2, 2, 6, false"
    })
    void testMoveUnitFailure(String unitType, int startX, int startY, int newX, int newY, boolean expectedResult) {
        Unit unitToMove;
        switch (unitType) {
            case "Archer":
                unitToMove = archer;
                break;
            case "Cannon":
                unitToMove = cannon;
                break;
            case "Transport":
                unitToMove = transport;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit type");
        }

        unitToMove.setX(startX);
        unitToMove.setY(startY);

        when(playerService.getPlayerByColorAndSession(anyString(), any(GameSession.class))).thenReturn(player);
        when(playerService.canExecuteCommand(any(Player.class))).thenReturn(true);

        boardService.moveUnit(board, "white", unitToMove.getId(), newX, newY);


            assertEquals(startX, unitToMove.getX());
            assertEquals(startY, unitToMove.getY());

    }

    @Test
    void testMoveUnitToOccupiedPositionBySamePlayer() {
        Unit anotherArcher = new Archer();
        anotherArcher.setId(6L);
        anotherArcher.setX(1);
        anotherArcher.setY(0);
        anotherArcher.setPlayer(player);
        anotherArcher.setGameSession(gameSession);

        board.addUnit(anotherArcher);


        boolean result = boardService.moveUnit(board, "white", archer.getId(), 1, 0);
        assertFalse(result);
    }

    @Test
    void testMoveUnitToOccupiedPositionByOpponent() {

        Player opponent = new Player("Player2", "black", gameSession);
        Unit opponentArcher = new Archer("Archer", 1, 0, opponent, gameSession);
        opponentArcher.setId(6L);

        board.addUnit(opponentArcher);


        when(playerService.getPlayerByColorAndSession(anyString(), any(GameSession.class))).thenReturn(player);
        when(playerService.canExecuteCommand(any(Player.class))).thenReturn(true);


        boolean result = boardService.moveUnit(board, "white", archer.getId(), 1, 0);


        assertFalse(result);


        assertEquals(0, archer.getX());
        assertEquals(0, archer.getY());
    }

    @Test
    void testTransportMoveToOccupiedPositionByOpponent() {

        Player opponent = new Player("Player2", "black", gameSession);
        Unit opponentArcher = new Archer("Archer", 3, 2, opponent, gameSession);
        opponentArcher.setId(6L);

        board.addUnit(opponentArcher);


        when(playerService.getPlayerByColorAndSession(anyString(), any(GameSession.class))).thenReturn(player);
        when(playerService.canExecuteCommand(any(Player.class))).thenReturn(true);


        boolean result = boardService.moveUnit(board, "white", transport.getId(), 3, 2);


        assertTrue(result);


        assertEquals(3, transport.getX());
        assertEquals(2, transport.getY());
        assertTrue(opponentArcher.isDestroyed());
    }

    @Test
    void testTransportMoveToOccupiedPositionByFriendlyUnit() {

        Unit friendlyArcher = new Archer("Archer", 3, 2, player, gameSession);
        friendlyArcher.setId(6L);

        board.addUnit(friendlyArcher);


        when(playerService.getPlayerByColorAndSession(anyString(), any(GameSession.class))).thenReturn(player);
        when(playerService.canExecuteCommand(any(Player.class))).thenReturn(true);


        boolean result = boardService.moveUnit(board, "white", transport.getId(), 3, 2);


        assertFalse(result);


        assertEquals(2, transport.getX());
        assertEquals(2, transport.getY());
        assertFalse(friendlyArcher.isDestroyed());
    }

    @Test
    void testTransportMoveToEnemyOccupiedPosition() {
        Player enemyPlayer = new Player("EnemyPlayer", "black", gameSession);
        Unit enemyArcher = new Archer("Archer", 2, 3, enemyPlayer, gameSession);
        setUnitId(enemyArcher, 4L);
        board.addUnit(enemyArcher);

        when(playerService.getPlayerByColorAndSession("white", gameSession)).thenReturn(player);
        when(playerService.canExecuteCommand(player)).thenReturn(true);
        when(unitRepository.save(any(Unit.class))).thenReturn(enemyArcher);
        when(commandHistoryRepository.save(any(CommandHistory.class))).thenReturn(null);

        boolean result = boardService.moveUnit(board, "white", transport.getId(), 2, 3);
        assertTrue(result);
        assertEquals(2, transport.getX());
        assertEquals(3, transport.getY());
        assertTrue(enemyArcher.isDestroyed());
    }



}
