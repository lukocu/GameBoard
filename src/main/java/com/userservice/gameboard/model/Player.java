package com.userservice.gameboard.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;;

    @ManyToOne
    @JoinColumn(name = "game_session_id")
    private GameSession gameSession;

    @ElementCollection
    private Map<String, LocalDateTime> lastCommandTimes = new HashMap<>();

    private LocalDateTime lastCommandTime;
    private Long commandCooldown;

    public Player() {}

    public Player(String name, String color, GameSession gameSession) {
        this.name = name;
        this.color = color;
        this.gameSession = gameSession;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return name.equals(player.name) && color.equals(player.color);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }

    public void setGameSession(GameSession currentGameSession) {
        this.gameSession=currentGameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastCommandTime(LocalDateTime lastCommandTime) {
        this.lastCommandTime = lastCommandTime;
    }

    public LocalDateTime getLastCommandTime() {
        return lastCommandTime;
    }

    public Long getCommandCooldown() {
        return commandCooldown;
    }

    public void setCommandCooldown(Long commandCooldown) {
        this.commandCooldown = commandCooldown;
    }

    public Map<String, LocalDateTime> getLastCommandTimes() {
        return lastCommandTimes;
    }


}
