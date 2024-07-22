package com.userservice.gameboard.model;

import jakarta.persistence.*;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "unit_type")
public abstract class Unit implements Movable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private String type;
    private int x;
    private int y;

    @ManyToOne
    @JoinColumn(name = "game_session_id")
    private GameSession gameSession;

    private boolean destroyed = false;

    public Unit() {
    }

    public Unit(String type, int x, int y, Player player, GameSession gameSession) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.player = player;
        this.gameSession = gameSession;
    }

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract boolean canMove(int newX, int newY, int boardWidth, int boardHeight);
    public abstract boolean canShoot(int startX, int startY, int targetX, int targetY);

    public void move(int newX, int newY) {
        setX(newX);
        setY(newY);
    }

    public void destroy() {
        setDestroyed(true);
        setX(-1);  // Można ustawić na dowolne koordynaty poza planszą
        setY(-1);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
