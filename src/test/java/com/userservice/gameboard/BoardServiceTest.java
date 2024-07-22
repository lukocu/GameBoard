package com.userservice.gameboard;

import com.userservice.gameboard.model.*;
import com.userservice.gameboard.repository.CommandHistoryRepository;
import com.userservice.gameboard.repository.UnitRepository;
import com.userservice.gameboard.service.BoardService;
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
import static org.mockito.Mockito.*;

public class BoardServiceTest {

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        gameSession = new GameSession();
        player = new Player("Player1", "white", gameSession);
        board = new Board(16, 16);
        board.setGameSession(gameSession);
    }


    @Test
    void testMoveUnit_CooldownNotExpired() {
        Unit unit = mock(Unit.class);
        when(unit.getId()).thenReturn(1L);
        when(unit.getPlayer()).thenReturn(player);

        board.addUnit(unit);

        when(playerService.getPlayerByColorAndSession("white", gameSession)).thenReturn(player);
        when(unitRepository.findById(1L)).thenReturn(Optional.of(unit));
        when(playerService.canExecuteCommand(player)).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            boardService.moveUnit(board, "white", 1L, 2, 2);
        });

        assertEquals("Command cooldown not yet expired", exception.getMessage());
    }



}
