package com.gotocompany.cards.dto;

/**
 * Response DTO for deck creation.
 */
public class CreateDeckResponse {
    private String deckId;

    public CreateDeckResponse() {
    }

    public CreateDeckResponse(String deckId) {
        this.deckId = deckId;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }
}

