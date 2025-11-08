package com.gotocompany.cards.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for removing a player from a game.
 */
public class RemovePlayerRequest {
    @NotBlank(message = "Player name cannot be blank")
    private String playerName;

    public RemovePlayerRequest() {
    }

    public RemovePlayerRequest(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
