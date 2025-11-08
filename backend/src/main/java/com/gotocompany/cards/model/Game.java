package com.gotocompany.cards.model;

import com.gotocompany.cards.util.ShuffleUtil;
import java.util.*;

/**
 * Represents a game with a shoe (game deck) consisting of multiple decks
 * and a group of players.
 */
public class Game {
    private final String id;
    private final List<Card> shoe; // The game deck (shoe) containing cards from all added decks
    private final Map<String, Player> players;
    private final Set<String> addedDeckIds; // Track which decks have been added

    public Game(String id) {
        this.id = id;
        this.shoe = new ArrayList<>();
        this.players = new HashMap<>();
        this.addedDeckIds = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    /**
     * Returns a copy of the shoe (game deck).
     */
    public List<Card> getShoe() {
        return new ArrayList<>(shoe);
    }

    /**
     * Returns the number of cards remaining in the shoe.
     */
    public int getShoeSize() {
        return shoe.size();
    }

    /**
     * Adds all cards from a deck to the shoe.
     * Once a deck is added, it cannot be removed (as per requirements).
     */
    public void addDeck(Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null");
        }
        String deckId = deck.getId();
        if (addedDeckIds.contains(deckId)) {
            throw new IllegalStateException("Deck " + deckId + " has already been added to the game");
        }
        shoe.addAll(deck.getCards());
        addedDeckIds.add(deckId);
    }

    /**
     * Checks if a deck has been added to this game.
     */
    public boolean isDeckAdded(String deckId) {
        return addedDeckIds.contains(deckId);
    }

    /**
     * Returns the IDs of all decks added to this game.
     */
    public Set<String> getAddedDeckIds() {
        return new HashSet<>(addedDeckIds);
    }

    /**
     * Adds a player to the game.
     */
    public void addPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        players.put(player.getName(), player);
    }

    /**
     * Removes a player from the game.
     */
    public void removePlayer(String playerName) {
        Player player = players.remove(playerName);
        if (player == null) {
            throw new IllegalArgumentException("Player " + playerName + " not found in game");
        }
        shoe.addAll(player.releaseHand());
    }

    /**
     * Gets a player by name.
     */
    public Player getPlayer(String playerName) {
        return players.get(playerName);
    }

    /**
     * Returns a copy of all players in the game.
     */
    public Collection<Player> getPlayers() {
        return new ArrayList<>(players.values());
    }

    /**
     * Deals the specified number of cards from the shoe to a player.
     * Returns the list of cards dealt.
     */
    public List<Card> dealCards(String playerName, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        if (count > shoe.size()) {
            throw new IllegalStateException("Not enough cards in shoe. Requested: " + count + ", Available: " + shoe.size());
        }
        
        Player player = players.get(playerName);
        if (player == null) {
            throw new IllegalArgumentException("Player " + playerName + " not found in game");
        }

        List<Card> dealtCards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (shoe.isEmpty()) {
                break; // No more cards to deal
            }
            Card card = shoe.remove(0); // Remove from front (top of deck)
            dealtCards.add(card);
            player.addCard(card);
        }

        return dealtCards;
    }

    /**
     * Shuffles the shoe using the Fisher-Yates algorithm.
     * This method can be called at any time to randomize the order of cards in the shoe.
     */
    public void shuffle() {
        if (shoe.isEmpty()) {
            return; // Nothing to shuffle
        }
        ShuffleUtil.shuffle(shoe);
    }

    @Override
    public String toString() {
        return "Game{id='" + id + "', shoeSize=" + shoe.size() + ", players=" + players.size() + "}";
    }
}

