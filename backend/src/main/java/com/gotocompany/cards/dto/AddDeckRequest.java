package com.gotocompany.cards.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for adding a deck to a game.
 */
public class AddDeckRequest {
    @NotBlank(message = "Deck ID cannot be blank")
    private String deckId;

    public AddDeckRequest() {
    }

    public AddDeckRequest(String deckId) {
        this.deckId = deckId;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }
}

