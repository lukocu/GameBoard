package com.userservice.gameboard.service;

import com.userservice.gameboard.model.Cannon;
import com.userservice.gameboard.model.GameSession;
import com.userservice.gameboard.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CannonTest {

    private Cannon cannon;
    private Player player;
    private GameSession gameSession;

    @BeforeEach
    void setUp() {
        player = new Player("Test Player", "white", gameSession);
        gameSession = new GameSession();
        cannon = new Cannon("Cannon", 4, 4, player, gameSession);
    }

    @Test
    void testCanMove_ShouldReturnFalse() {
        assertFalse(cannon.canMove(5, 5, 10, 10));
        assertFalse(cannon.canMove(4, 5, 10, 10));
        assertFalse(cannon.canMove(3, 4, 10, 10));
        assertFalse(cannon.canMove(4, 3, 10, 10));
        assertFalse(cannon.canMove(5, 4, 10, 10));
    }

    @Test
    void testCanShoot_ValidTarget_ShouldReturnTrue() {
        assertTrue(cannon.canShoot(4, 4, 4, 7));
        assertTrue(cannon.canShoot(4, 4, 7, 4));
        assertTrue(cannon.canShoot(4, 4, 7, 7));
        assertTrue(cannon.canShoot(4, 4, 1, 1));
    }

    @Test
    void testCanShoot_InvalidTarget_ShouldReturnFalse() {
        assertFalse(cannon.canShoot(4, 4, 5, 6));
        assertFalse(cannon.canShoot(4, 4, 3, 6));
    }
}
