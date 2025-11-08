package com.gotocompany.cards.model;

import com.gotocompany.cards.model.enums.FaceValue;
import com.gotocompany.cards.model.enums.Suit;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a standard 52-card deck of playing cards.
 * Each deck contains one card of each suit and face value combination.
 */
public class Deck {
    private final String id;
    private final List<Card> cards;

    public Deck(String id) {
        this.id = id;
        this.cards = createStandardDeck();
    }

    /**
     * Creates a standard 52-card deck with all suits and face values.
     */
    private List<Card> createStandardDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (FaceValue faceValue : FaceValue.values()) {
                deck.add(new Card(suit, faceValue));
            }
        }
        return deck;
    }

    public String getId() {
        return id;
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards); // Return copy to maintain immutability
    }

    /**
     * Returns the number of cards in the deck.
     */
    public int size() {
        return cards.size();
    }

    @Override
    public String toString() {
        return "Deck{id='" + id + "', size=" + cards.size() + "}";
    }
}

