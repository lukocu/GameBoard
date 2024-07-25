package com.userservice.gameboard.service;

import com.userservice.gameboard.model.Archer;
import com.userservice.gameboard.model.GameSession;
import com.userservice.gameboard.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArcherTest {

    private Archer archer;
    private Player player;
    private GameSession gameSession;

    @BeforeEach
    void setUp() {
        player = new Player("Test Player", "white", gameSession);
        gameSession = new GameSession();
        archer = new Archer("Archer", 2, 2, player, gameSession);
    }

    @Test
    void testCanMove_ValidMove_ShouldReturnTrue() {
        assertTrue(archer.canMove(3, 2, 10, 10));
        assertTrue(archer.canMove(2, 3, 10, 10));
        assertTrue(archer.canMove(1, 2, 10, 10));
        assertTrue(archer.canMove(2, 1, 10, 10));
    }

    @Test
    void testCanMove_InvalidMove_ShouldReturnFalse() {
        assertFalse(archer.canMove(4, 2, 10, 10));
        assertFalse(archer.canMove(2, 5, 10, 10));
        assertFalse(archer.canMove(0, 2, 10, 10));
        assertFalse(archer.canMove(2, 0, 10, 10));
    }

    @Test
    void testCanMove_OutOfBounds_ShouldReturnFalse() {
        assertFalse(archer.canMove(-1, 2, 10, 10));
        assertFalse(archer.canMove(2, -1, 10, 10));
        assertFalse(archer.canMove(10, 2, 10, 10));
        assertFalse(archer.canMove(2, 10, 10, 10));
    }

    @Test
    void testCanShoot_ValidTarget_ShouldReturnTrue() {
        assertTrue(archer.canShoot(2, 2, 2, 5));
        assertTrue(archer.canShoot(2, 2, 5, 2));
    }

    @Test
    void testCanShoot_InvalidTarget_ShouldReturnFalse() {
        assertFalse(archer.canShoot(2, 2, 3, 3));         assertFalse(archer.canShoot(2, 2, 3, 4));
    }
}
