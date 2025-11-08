package com.gotocompany.cards.model;

import com.gotocompany.cards.model.enums.FaceValue;
import com.gotocompany.cards.model.enums.Suit;

import java.util.Objects;

/**
 * Represents a playing card with a suit and face value.
 * Cards are immutable to ensure consistency and thread safety.
 */
public class Card {
    private final Suit suit;
    private final FaceValue faceValue;

    public Card(Suit suit, FaceValue faceValue) {
        if (suit == null || faceValue == null) {
            throw new IllegalArgumentException("Suit and face value cannot be null");
        }
        this.suit = suit;
        this.faceValue = faceValue;
    }

    public Suit getSuit() {
        return suit;
    }

    public FaceValue getFaceValue() {
        return faceValue;
    }

    /**
     * Returns the numeric value of the card for scoring purposes.
     * Uses face values only (Ace=1, 2-10=face value, Jack=11, Queen=12, King=13).
     */
    public int getValue() {
        return faceValue.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && faceValue == card.faceValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, faceValue);
    }

    @Override
    public String toString() {
        return faceValue.getDisplayName() + " of " + suit.getDisplayName();
    }
}

