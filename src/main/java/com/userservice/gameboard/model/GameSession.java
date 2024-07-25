package com.userservice.gameboard.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @OneToOne(mappedBy = "gameSession", cascade = CascadeType.ALL)
    private Board board;
    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();
    public GameSession() {
        this.startTime = new Date();
    }

    public GameSession(Board board) {
        this.startTime = new Date();
        this.board=board;

    }

    public Long getId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public void addPlayer(Player player) {
        players.add(player);
        player.setGameSession(this);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.setGameSession(null);
    }
}
