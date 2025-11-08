package com.gotocompany.cards.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a player in the game with a hand of cards.
 */
public class Player {
    private final String name;
    private final List<Card> hand;

    public Player(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    /**
     * Returns a copy of the player's hand.
     */
    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    /**
     * Adds a card to the player's hand.
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        hand.add(card);
    }

    /**
     * Adds multiple cards to the player's hand.
     */
    public void addCards(List<Card> cards) {
        if (cards == null) {
            throw new IllegalArgumentException("Cards list cannot be null");
        }
        hand.addAll(cards);
    }

    /**
     * Calculates the total value of all cards in the player's hand.
     * Uses face values only (Ace=1, 2-10=face value, Jack=11, Queen=12, King=13).
     */
    public int getHandValue() {
        return hand.stream()
                .mapToInt(Card::getValue)
                .sum();
    }

    /**
     * Returns the number of cards in the player's hand.
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Returns all cards currently held and clears the player's hand.
     */
    public List<Card> releaseHand() {
        List<Card> cards = new ArrayList<>(hand);
        hand.clear();
        return cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "', handSize=" + hand.size() + ", handValue=" + getHandValue() + "}";
    }
}

