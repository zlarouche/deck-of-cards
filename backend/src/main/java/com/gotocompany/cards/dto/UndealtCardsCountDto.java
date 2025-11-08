package com.gotocompany.cards.dto;

import com.gotocompany.cards.model.enums.FaceValue;
import com.gotocompany.cards.model.enums.Suit;

import java.util.Map;

/**
 * DTO for undealt cards count sorted by suit and face value.
 */
public class UndealtCardsCountDto {
    private Map<Suit, Map<FaceValue, Integer>> cardCounts;

    public UndealtCardsCountDto() {
    }

    public UndealtCardsCountDto(Map<Suit, Map<FaceValue, Integer>> cardCounts) {
        this.cardCounts = cardCounts;
    }

    public Map<Suit, Map<FaceValue, Integer>> getCardCounts() {
        return cardCounts;
    }

    public void setCardCounts(Map<Suit, Map<FaceValue, Integer>> cardCounts) {
        this.cardCounts = cardCounts;
    }
}

