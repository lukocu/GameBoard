package com.userservice.gameboard.model;

public interface Movable {
    boolean canMove(int newX, int newY, int boardWidth, int boardHeight);
    void move(int newX, int newY);
    boolean canShoot(int startX, int startY, int targetX, int targetY);
}
