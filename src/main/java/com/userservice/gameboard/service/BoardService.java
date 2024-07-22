package com.userservice.gameboard.service;

import com.userservice.gameboard.model.*;
import com.userservice.gameboard.repository.CommandHistoryRepository;
import com.userservice.gameboard.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private CommandHistoryRepository commandHistoryRepository;

    @Autowired
    private PlayerService playerService;

    String commandType = "";

    public boolean moveUnit(Board board, String playerColor, Long unitId, int newX, int newY) {
        Player player = playerService.getPlayerByColorAndSession(playerColor, board.getGameSession());

        Unit unitToMove = null;
        Unit targetUnit = null;
        commandType="";

        for (Unit unit : board.getUnits()) {
            if (unit.getId().equals(unitId) && unit.getPlayer().equals(player)) {
                unitToMove = unit;
                if (unitToMove instanceof Archer) {
                    commandType = "moveArcher";
                } else if (unitToMove instanceof Transport) {
                    commandType = "moveTransport";
                }

            }
            if (unit.getX() == newX && unit.getY() == newY) {
                targetUnit = unit;
            }
        }

        if (unitToMove == null) {
            return false;
        }

        if (!playerService.canExecuteCommand(player)) {
            throw new IllegalStateException("Command cooldown not yet expired");
        }

        if (unitToMove.isDestroyed()) {
            throw new IllegalArgumentException("Cannot move a destroyed unit");
        }

        if (targetUnit != null && targetUnit.getPlayer().equals(player)) {
            return false;
        }

        if (unitToMove.canMove(newX, newY, board.getWidth(), board.getHeight())) {
            if (targetUnit != null && !targetUnit.getPlayer().equals(player) && unitToMove instanceof Transport) {
                targetUnit.destroy();
                unitRepository.save(targetUnit);
                commandHistoryRepository.save(new CommandHistory("Player color: "+ playerColor+" Unit [" + targetUnit.getClass().getSimpleName() + ", " + targetUnit.getPlayer().getColor() + "] was destroyed by transport", board.getGameSession()));
            } else if (targetUnit != null && !targetUnit.getPlayer().equals(player)) {
                return false;
            }

            unitToMove.move(newX, newY);
            unitRepository.save(unitToMove);
            commandHistoryRepository.save(new CommandHistory("Color player " + playerColor + " move " + unitId + " to " + newX + "," + newY, board.getGameSession()));
            playerService.updateLastCommandTime(player, commandType);

            return true;
        }

        return false;
    }

    public String shoot(Board board, String playerColor, Long unitId, int targetX, int targetY) {
        if (targetX < 0 || targetX >= board.getWidth() || targetY < 0 || targetY >= board.getHeight()) {
            return "Target out of bounds";
        }

        Player player = playerService.getPlayerByColorAndSession(playerColor, board.getGameSession());

        Unit shooter = unitRepository.findById(unitId).orElse(null);
        if (shooter == null || !shooter.getPlayer().equals(player)) {
            return "Shooter unit not found or does not belong to the player";
        }

        commandType="";
        if (shooter instanceof Archer) {
            commandType = "shootArcher";
        } else if (shooter instanceof Cannon) {
            commandType = "shootCannon";
        }

        if (!playerService.canExecuteCommand(player)) {
            throw new IllegalStateException("Command cooldown not yet expired");
        }

        if (shooter.isDestroyed()) {
            throw new IllegalArgumentException("Cannot use a destroyed unit to shoot");
        }

        Unit target = null;
        for (Unit unit : board.getUnits()) {
            if (unit.getX() == targetX && unit.getY() == targetY) {
                target = unit;
                break;
            }
        }

        if (target != null && shooter.getPlayer().equals(target.getPlayer())) {
            return "Cannot shoot at own units";
        }

        if (shooter.canShoot(shooter.getX(), shooter.getY(), targetX, targetY)) {
            if (target != null) {
                target.destroy();
                unitRepository.save(target);
                commandHistoryRepository.save(new CommandHistory("Color player " + playerColor + " shoot " + unitId + " at " + targetX + "," + targetY, board.getGameSession()));
                commandHistoryRepository.save(new CommandHistory("Unit [" + target.getClass().getSimpleName() + ", " + target.getPlayer().getColor() + "] was destroyed", board.getGameSession()));
                playerService.updateLastCommandTime(player, commandType);
                return "Unit [" + target.getClass().getSimpleName() + ", " + target.getPlayer().getColor() + "] was destroyed";
            }
        }
        commandHistoryRepository.save(new CommandHistory("Color player " + playerColor + " Shot missed " + " at " + targetX + "," + targetY, board.getGameSession()));
        playerService.updateLastCommandTime(player, commandType);
        return "Shot missed";
    }
}
