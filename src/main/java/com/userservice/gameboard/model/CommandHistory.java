package com.userservice.gameboard.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class CommandHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String command;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "game_session_id")
    private GameSession gameSession;

    public CommandHistory() {
        this.timestamp = new Date();
    }

    public CommandHistory(String command, GameSession gameSession) {
        this.command = command;
        this.gameSession = gameSession;
        this.timestamp = new Date();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }
}
