package com.userservice.gameboard.dto;

public class UnitDTO {
    private Long id;
    private String type;
    private int x;
    private int y;
    private boolean destroyed;
    private PlayerDTO player;

    // Prywatny konstruktor, używany przez Builder
    private UnitDTO(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.x = builder.x;
        this.y = builder.y;
        this.destroyed = builder.destroyed;
        this.player = builder.player;
    }

    // Gettery
    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    // Builder
    public static class Builder {
        private Long id;
        private String type;
        private int x;
        private int y;
        private boolean destroyed;
        private PlayerDTO player;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder destroyed(boolean destroyed) {
            this.destroyed = destroyed;
            return this;
        }

        public Builder player(PlayerDTO player) {
            this.player = player;
            return this;
        }

        public UnitDTO build() {
            return new UnitDTO(this);
        }
    }
}
