package com.userservice.gameboard.service;

import com.userservice.gameboard.configuration.BoardConfiguration;
import com.userservice.gameboard.dto.mappers.BoardMapper;
import com.userservice.gameboard.model.*;
import com.userservice.gameboard.repository.BoardRepository;
import com.userservice.gameboard.repository.CommandHistoryRepository;
import com.userservice.gameboard.repository.PlayerRepository;
import com.userservice.gameboard.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private BoardMapper boardMapper;
    @Mock
    private BoardConfiguration boardConfiguration;

    @InjectMocks
    private BattleService battleService;

    private Unit cannon;
    private Unit transport;
    private GameSession gameSession;
    private Board board;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        gameSession = mock(GameSession.class);
        board = mock(Board.class);

        when(board.getWidth()).thenReturn(10);
        when(board.getHeight()).thenReturn(10);
        when(gameSession.getBoard()).thenReturn(board);

        Player player = new Player("Test Player", "white", gameSession);
        cannon = new Cannon("Cannon", 1, 1, player, gameSession);
        transport = new Transport("Transport", 2, 2, player, gameSession);

        when(unitRepository.findById(cannon.getId())).thenReturn(Optional.of(cannon));
        when(unitRepository.findById(transport.getId())).thenReturn(Optional.of(transport));
        when(gameSessionService.getGameSession(anyLong())).thenReturn(gameSession);
    }

    @Test
    void moveUnitRandomly_TransportCanMove_ShouldMoveUnit() {
        when(boardService.moveUnit(any(), anyString(), eq(transport.getId()), anyInt(), anyInt())).thenReturn(true);

        String result = battleService.moveUnitRandomly("white", transport.getId());

        assertTrue(result.startsWith("Unit " + transport.getId() + " moved to"));
        verify(boardService, atLeastOnce()).moveUnit(any(), eq("white"), eq(transport.getId()), anyInt(), anyInt());
    }

    @Test
    void moveUnitRandomly_TransportCannotMove_ShouldReturnCouldNotMove() {
        when(boardService.moveUnit(any(), anyString(), eq(transport.getId()), anyInt(), anyInt())).thenReturn(false);

        String result = battleService.moveUnitRandomly("white", transport.getId());

        assertEquals("Unit " + transport.getId() + " could not move", result);
    }
}
