package com.gotocompany.cards.repository;

import com.gotocompany.cards.model.Deck;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository for managing decks in-memory.
 * Uses ConcurrentHashMap for thread-safe operations.
 */
@Repository
public class DeckRepository {

    private final Map<String, Deck> decks = new ConcurrentHashMap<>();

    /**
     * Saves a deck to the repository.
     */
    public Deck save(Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null");
        }
        decks.put(deck.getId(), deck);
        return deck;
    }

    /**
     * Finds a deck by ID.
     */
    public Optional<Deck> findById(String id) {
        return Optional.ofNullable(decks.get(id));
    }

    /**
     * Checks if a deck exists.
     */
    public boolean existsById(String id) {
        return decks.containsKey(id);
    }

    /**
     * Returns all decks.
     */
    public Map<String, Deck> findAll() {
        return new ConcurrentHashMap<>(decks);
    }
}

