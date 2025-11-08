package com.gotocompany.cards.model.enums;

/**
 * Represents the face values of cards in a standard deck.
 * Ace has a value of 1, number cards have their face value,
 * and face cards (Jack, Queen, King) have values 11, 12, 13 respectively.
 */
public enum FaceValue {
    ACE("Ace", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("Jack", 11),
    QUEEN("Queen", 12),
    KING("King", 13);

    private final String displayName;
    private final int value;

    FaceValue(String displayName, int value) {
        this.displayName = displayName;
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getValue() {
        return value;
    }
}

