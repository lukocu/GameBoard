package com.userservice.gameboard.configuration;

import com.userservice.gameboard.model.Board;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoardConfiguration {

    @Value("${board.width}")
    private int width;

    @Value("${board.height}")
    private int height;

    @Value("${units.archers}")
    private int archerCount;

    @Value("${units.cannons}")
    private int cannonCount;

    @Value("${units.transports}")
    private int transportCount;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getArcherCount() {
        return archerCount;
    }

    public int getCannonCount() {
        return cannonCount;
    }

    public int getTransportCount() {
        return transportCount;
    }

    @Bean
    public Board board() {
        return new Board(width, height);
    }
}
