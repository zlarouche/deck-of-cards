package com.gotocompany.cards.dto;

import com.gotocompany.cards.model.enums.Suit;

import java.util.Map;

/**
 * DTO for undealt cards grouped by suit.
 */
public class UndealtCardsBySuitDto {
    private Map<Suit, Integer> suitCounts;

    public UndealtCardsBySuitDto() {
    }

    public UndealtCardsBySuitDto(Map<Suit, Integer> suitCounts) {
        this.suitCounts = suitCounts;
    }

    public Map<Suit, Integer> getSuitCounts() {
        return suitCounts;
    }

    public void setSuitCounts(Map<Suit, Integer> suitCounts) {
        this.suitCounts = suitCounts;
    }
}

