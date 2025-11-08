package com.gotocompany.cards.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for dealing cards to a player.
 */
public class DealCardsRequest {
    @NotBlank(message = "Player name cannot be blank")
    private String playerName;

    @Min(value = 1, message = "Count must be at least 1")
    private int count;

    public DealCardsRequest() {
    }

    public DealCardsRequest(String playerName, int count) {
        this.playerName = playerName;
        this.count = count;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

