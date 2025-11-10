package com.gotocompany.cards.dto;

/**
 * Response DTO for game creation.
 */
public class CreateGameResponse {
    private String gameId;
    private String name;

    public CreateGameResponse() {
    }

    public CreateGameResponse(String gameId, String name) {
        this.gameId = gameId;
        this.name = name;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

