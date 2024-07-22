package com.userservice.gameboard;

import com.userservice.gameboard.configuration.BoardConfiguration;
import com.userservice.gameboard.dto.BoardDTO;
import com.userservice.gameboard.dto.mappers.BoardMapper;
import com.userservice.gameboard.model.*;
import com.userservice.gameboard.repository.BoardRepository;
import com.userservice.gameboard.repository.CommandHistoryRepository;
import com.userservice.gameboard.repository.PlayerRepository;
import com.userservice.gameboard.repository.UnitRepository;
import com.userservice.gameboard.service.BattleService;
import com.userservice.gameboard.service.BoardService;
import com.userservice.gameboard.service.GameSessionService;
import com.userservice.gameboard.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BattleServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private GameSessionService gameSessionService;

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private BoardService boardService;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private CommandHistoryRepository commandHistoryRepository;

    @Mock
    private BoardConfiguration boardConfiguration;

    @Mock
    private BoardMapper boardMapper;

    @InjectMocks
    private BattleService battleService;

    private Player player1;
    private Player player2;
    private Board board;
    private GameSession gameSession;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        player1 = new Player("Player1", "white", null);
        player2 = new Player("Player2", "black", null);
        board = new Board(16, 16);
        gameSession = new GameSession();

        player1.setGameSession(gameSession);
        player2.setGameSession(gameSession);

        when(boardConfiguration.getWidth()).thenReturn(16);
        when(boardConfiguration.getHeight()).thenReturn(16);
        when(gameSessionService.startNewGame(any(Board.class))).thenReturn(gameSession);
        when(boardMapper.toDTO(any(Board.class))).thenReturn(new BoardDTO());

        battleService.setCurrentGameSession(gameSession);  // Ustawienie currentGameSession
    }


    @Test
    void moveUnitFailTest() {
        when(boardRepository.findByGameSessionId(anyLong())).thenReturn(null);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            battleService.moveUnit("white", 1L, 1, 1);
        });

        assertEquals("No board found for the current game session", exception.getMessage());
    }

    @Test
    void shootTest() {
        when(boardRepository.findByGameSessionId(anyLong())).thenReturn(board);
        when(boardService.shoot(any(Board.class), anyString(), anyLong(), anyInt(), anyInt())).thenReturn("Shot missed");

        String result = battleService.shoot("white", 1L, 1, 1);

        assertEquals("Shot missed", result);
        verify(commandHistoryRepository, times(1)).save(any(CommandHistory.class));
    }

    @Test
    void moveUnitRandomlyTest() {
        Unit archer = new Archer("Archer", 0, 0, player1, gameSession);
        when(unitRepository.findById(anyLong())).thenReturn(Optional.of(archer));
        when(boardService.moveUnit(any(Board.class), anyString(), anyLong(), anyInt(), anyInt())).thenReturn(true);

        String result = battleService.moveUnitRandomly("white", 1L);

        assertNotNull(result);
        assertTrue(result.contains("moved to"));
    }

    @Test
    void moveUnitRandomlyCannonTest() {
        Unit cannon = new Cannon("Cannon", 0, 0, player1, gameSession);
        when(unitRepository.findById(anyLong())).thenReturn(Optional.of(cannon));

        String result = battleService.moveUnitRandomly("white", 1L);

        assertEquals("Cannon cannot move", result);
    }
}
