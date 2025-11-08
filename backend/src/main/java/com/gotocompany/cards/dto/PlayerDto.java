package com.gotocompany.cards.dto;

import com.gotocompany.cards.model.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for player representation in API responses.
 */
public class PlayerDto {
    private String name;
    private List<CardDto> hand;
    private int handValue;
    private int handSize;

    public PlayerDto() {
    }

    public PlayerDto(Player player) {
        this.name = player.getName();
        this.hand = player.getHand().stream()
                .map(CardDto::new)
                .collect(Collectors.toList());
        this.handValue = player.getHandValue();
        this.handSize = player.getHandSize();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CardDto> getHand() {
        return hand;
    }

    public void setHand(List<CardDto> hand) {
        this.hand = hand;
    }

    public int getHandValue() {
        return handValue;
    }

    public void setHandValue(int handValue) {
        this.handValue = handValue;
    }

    public int getHandSize() {
        return handSize;
    }

    public void setHandSize(int handSize) {
        this.handSize = handSize;
    }
}

