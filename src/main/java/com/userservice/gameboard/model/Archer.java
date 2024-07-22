package com.userservice.gameboard.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Archer")
public class Archer extends Unit {

    public Archer() {
    }

    public Archer(String type, int x, int y, Player player, GameSession gameSession) {
        super(type, x, y, player, gameSession);
    }

    @Override
    public boolean canMove(int newX, int newY, int boardWidth, int boardHeight) {
        // Check if the move is within the bounds of the board
        if (newX < 0 || newX >= boardWidth || newY < 0 || newY >= boardHeight) {
            return false;
        }


        int deltaX = Math.abs(newX - getX());
        int deltaY = Math.abs(newY - getY());

        return (deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1);
    }

    @Override
    public boolean canShoot(int startX, int startY, int targetX, int targetY) {

        return (startX == targetX || startY == targetY);
    }
}
