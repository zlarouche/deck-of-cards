package com.gotocompany.cards.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for adding a player to a game.
 */
public class AddPlayerRequest {
    @NotBlank(message = "Player name cannot be blank")
    private String playerName;

    public AddPlayerRequest() {
    }

    public AddPlayerRequest(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}

