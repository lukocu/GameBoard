package com.userservice.gameboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.userservice.gameboard.model.GameSession;
import com.userservice.gameboard.model.Player;
import com.userservice.gameboard.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlayers_shouldSaveBothPlayers() {
        Player player1 = new Player();
        Player player2 = new Player();

        playerService.createPlayers(player1, player2);

        verify(playerRepository, times(2)).save(any(Player.class));
    }

    @Test
    void playerExists_shouldReturnTrueIfPlayerExists() {
        Player player = new Player();
        player.setId(1L);
        when(playerRepository.existsById(1L)).thenReturn(true);

        boolean exists = playerService.playerExists(player);

        assertTrue(exists);
    }

    @Test
    void playerExists_shouldReturnFalseIfPlayerDoesNotExist() {
        Player player = new Player();
        player.setId(1L);
        when(playerRepository.existsById(1L)).thenReturn(false);

        boolean exists = playerService.playerExists(player);

        assertFalse(exists);
    }

    @Test
    void getPlayers_shouldReturnListOfPlayers() {
        Player player = new Player();
        when(playerRepository.findAll()).thenReturn(Collections.singletonList(player));

        List<Player> players = playerService.getPlayers();

        assertNotNull(players);
        assertEquals(1, players.size());
        assertEquals(player, players.get(0));
    }

    @Test
    void isValidPlayer_shouldReturnTrueForValidColor() {
        assertTrue(playerService.isValidPlayer("white"));
        assertTrue(playerService.isValidPlayer("black"));
    }

    @Test
    void isValidPlayer_shouldReturnFalseForInvalidColor() {
        assertFalse(playerService.isValidPlayer("red"));
    }

    @Test
    void getPlayerByColorAndSession_shouldReturnPlayer() {
        GameSession session = new GameSession();
        session.setId(1L);
        Player player = new Player();
        player.setColor("white");
        player.setGameSession(session);
        when(playerRepository.findByColorAndGameSessionId("white", 1L)).thenReturn(Collections.singletonList(player));

        Player result = playerService.getPlayerByColorAndSession("white", session);

        assertEquals(player, result);
    }

    @Test
    void getPlayerByColorAndSession_shouldThrowExceptionIfNotFound() {
        GameSession session = new GameSession();
        session.setId(1L);
        when(playerRepository.findByColorAndGameSessionId("white", 1L)).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementException.class, () -> playerService.getPlayerByColorAndSession("white", session));
    }

    @Test
    void canExecuteCommand_shouldReturnTrueIfNoLastCommandTime() {
        Player player = new Player();

        boolean canExecute = playerService.canExecuteCommand(player);

        assertTrue(canExecute);
    }

    @Test
    void canExecuteCommand_shouldReturnTrueIfCooldownHasPassed() {
        Player player = new Player();
        player.setLastCommandTime(LocalDateTime.now().minusSeconds(10));
        player.setCommandCooldown(5L);

        boolean canExecute = playerService.canExecuteCommand(player);

        assertTrue(canExecute);
    }

    @Test
    void canExecuteCommand_shouldReturnFalseIfCooldownHasNotPassed() {
        Player player = new Player();
        player.setLastCommandTime(LocalDateTime.now());
        player.setCommandCooldown(10L);

        boolean canExecute = playerService.canExecuteCommand(player);

        assertFalse(canExecute);
    }

    @Test
    void updateLastCommandTime_shouldUpdateLastCommandTimeAndCooldown() {
        Player player = new Player();
        String commandType = "moveArcher";
        LocalDateTime now = LocalDateTime.now();

        playerService.updateLastCommandTime(player, commandType);

        assertEquals(now.withNano(0), player.getLastCommandTime().withNano(0));
        assertEquals((Long) 5L, player.getCommandCooldown());
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void updateLastCommandTime_shouldThrowExceptionForUnknownCommand() {
        Player player = new Player();
        String unknownCommand = "unknownCommand";

        assertThrows(IllegalArgumentException.class, () -> playerService.updateLastCommandTime(player, unknownCommand));
    }
}
