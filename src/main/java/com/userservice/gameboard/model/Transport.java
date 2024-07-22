package com.userservice.gameboard.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Transport")
public class Transport extends Unit {

    public Transport() {
    }

    public Transport(String type, int x, int y, Player player, GameSession gameSession) {
        super(type, x, y, player, gameSession);
    }

    @Override
    public boolean canMove(int newX, int newY, int boardWidth, int boardHeight) {

        if (newX < 0 || newX >= boardWidth || newY < 0 || newY >= boardHeight) {
            return false;
        }

        int deltaX = Math.abs(newX - getX());
        int deltaY = Math.abs(newY - getY());

        return (deltaX == 0 && deltaY > 0 && deltaY <= 3) || (deltaY == 0 && deltaX > 0 && deltaX <= 3);
    }

    @Override
    public boolean canShoot(int startX, int startY, int targetX, int targetY) {

        return false;
    }
}
