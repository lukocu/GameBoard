package com.userservice.gameboard.service;

import com.userservice.gameboard.model.GameSession;
import com.userservice.gameboard.model.Player;
import com.userservice.gameboard.model.Transport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransportTest {

    private Transport transport;
    private Player player;
    private GameSession gameSession;

    @BeforeEach
    void setUp() {
        player = new Player("Test Player", "white", gameSession);
        gameSession = new GameSession();
        transport = new Transport("Transport", 2, 2, player, gameSession);
    }

    @Test
    void testCanMove_ValidMove_ShouldReturnTrue() {

        assertTrue(transport.canMove(2, 3, 10, 10));
        assertTrue(transport.canMove(2, 5, 10, 10));
        assertTrue(transport.canMove(2, 0, 10, 10));


        assertTrue(transport.canMove(3, 2, 10, 10));
        assertTrue(transport.canMove(5, 2, 10, 10));
        assertTrue(transport.canMove(0, 2, 10, 10));
    }

    @Test
    void testCanMove_InvalidMove_ShouldReturnFalse() {
        assertFalse(transport.canMove(2, 6, 10, 10));
        assertFalse(transport.canMove(6, 2, 10, 10));
        assertFalse(transport.canMove(0, 6, 10, 10));
        assertFalse(transport.canMove(3, 3, 10, 10));
    }

    @Test
    void testCanMove_OutOfBounds_ShouldReturnFalse() {
        assertFalse(transport.canMove(-1, 2, 10, 10));
        assertFalse(transport.canMove(2, -1, 10, 10));
        assertFalse(transport.canMove(10, 2, 10, 10));
        assertFalse(transport.canMove(2, 10, 10, 10));
    }

    @Test
    void testCanShoot_ShouldReturnFalse() {
        assertFalse(transport.canShoot(2, 2, 2, 5));
        assertFalse(transport.canShoot(2, 2, 5, 2));
        assertFalse(transport.canShoot(2, 2, 3, 3));
    }
}
