package com.gotocompany.cards.dto;

import com.gotocompany.cards.model.Card;
import com.gotocompany.cards.model.enums.FaceValue;
import com.gotocompany.cards.model.enums.Suit;

/**
 * DTO for card representation in API responses.
 */
public class CardDto {
    private Suit suit;
    private FaceValue faceValue;
    private int value;
    private String displayName;

    public CardDto() {
    }

    public CardDto(Card card) {
        this.suit = card.getSuit();
        this.faceValue = card.getFaceValue();
        this.value = card.getValue();
        this.displayName = card.toString();
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public FaceValue getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(FaceValue faceValue) {
        this.faceValue = faceValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

