package com.userservice.gameboard.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int width;
    private int height;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Map<Long, Unit> unitsById = new HashMap<>();

    @OneToOne
    @JoinColumn(name = "game_session_id")
    private GameSession gameSession;

    @Transient
    private Map<Point, Unit> unitsByPosition = new HashMap<>();

    public Board() {
    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @PostLoad
    private void populateUnitsByPosition() {
        for (Unit unit : unitsById.values()) {
            unitsByPosition.put(getPositionKey(unit.getX(), unit.getY()), unit);
        }
    }

    private Point getPositionKey(int x, int y) {
        return new Point(x, y);
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

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Unit getUnitById(Long id) {
        return unitsById.get(id);
    }

    public void addUnit(Unit unit) {
        unitsById.put(unit.getId(), unit);
        unitsByPosition.put(getPositionKey(unit.getX(), unit.getY()), unit);
    }

    public void removeUnit(Unit unit) {
        unitsById.remove(unit.getId());
        unitsByPosition.remove(getPositionKey(unit.getX(), unit.getY()));
    }
    public Unit getUnitByPosition(int x, int y) {
        return unitsByPosition.get(new Point(x, y));
    }

    public void updateUnitPosition(Unit unit, int oldX, int oldY, int newX, int newY) {
        unitsByPosition.remove(new Point(oldX, oldY));
        unitsByPosition.put(getPositionKey(newX, newY), unit);
    }

    public Map<Long, Unit> getUnitsById() {
        return unitsById;
    }

    public void setUnitsById(Map<Long, Unit> unitsById) {
        this.unitsById = unitsById;
    }

    public void setUnitsByPosition(Map<Point, Unit> unitsByPosition) {
        this.unitsByPosition = unitsByPosition;
    }


}