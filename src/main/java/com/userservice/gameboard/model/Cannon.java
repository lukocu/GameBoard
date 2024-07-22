package com.userservice.gameboard.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Cannon")
public class Cannon extends Unit {

    public Cannon() {
    }

    public Cannon(String type, int x, int y, Player player, GameSession gameSession) {
        super(type, x, y, player, gameSession);
    }

    @Override
    public boolean canMove(int newX, int newY, int boardWidth, int boardHeight) {

        return false;
    }

    @Override
    public boolean canShoot(int startX, int startY, int targetX, int targetY) {

        int deltaX = Math.abs(targetX - startX);
        int deltaY = Math.abs(targetY - startY);
        return (deltaX == 0 || deltaY == 0 || deltaX == deltaY);
    }
}
