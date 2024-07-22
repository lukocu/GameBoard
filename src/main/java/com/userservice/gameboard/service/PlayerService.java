package com.userservice.gameboard.service;

import com.userservice.gameboard.model.GameSession;
import com.userservice.gameboard.model.Player;
import com.userservice.gameboard.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    private final Map<String, Long> commandCooldowns = new HashMap<>();

    public PlayerService() {
        commandCooldowns.put("moveArcher", 5L);
        commandCooldowns.put("moveTransport", 7L);
        commandCooldowns.put("shootArcher", 10L);
        commandCooldowns.put("shootCannon", 13L);
    }

    public void createPlayers(Player player1, Player player2) {

        playerRepository.save(player1);
        playerRepository.save(player2);

    }

    public boolean playerExists(Player player) {
        return playerRepository.existsById(player.getId());
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public boolean isValidPlayer(String playerColor) {
        return playerColor.equalsIgnoreCase("white") || playerColor.equalsIgnoreCase("black");
    }

    public Player getPlayerByColorAndSession(String color, GameSession gameSession) {
        List<Player> players = playerRepository.findByColorAndGameSessionId(color, gameSession.getId());
        if (players.size() != 1) {
            throw new NoSuchElementException("There must be exactly one player with the color " + color + " in the session " + gameSession.getId());
        }
        return players.get(0);
    }

    public boolean canExecuteCommand(Player player) {
        LocalDateTime lastCommandTime = player.getLastCommandTime();
        if (lastCommandTime == null) {
            return true;
        }
        Duration duration = Duration.between(lastCommandTime, LocalDateTime.now());
        Long cooldown = player.getCommandCooldown();
        return duration.getSeconds() >= cooldown;
    }

    public void updateLastCommandTime(Player player, String commandType) {
        LocalDateTime now = LocalDateTime.now();
        Long cooldown = commandCooldowns.get(commandType);
        if (cooldown == null) {
            throw new IllegalArgumentException("Unknown command type: " + commandType);
        }
        player.setLastCommandTime(now);
        player.setCommandCooldown(cooldown);
        playerRepository.save(player);
    }


}
