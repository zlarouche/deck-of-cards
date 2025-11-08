package com.gotocompany.cards.service;

import com.gotocompany.cards.model.Deck;
import com.gotocompany.cards.repository.DeckRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for managing decks.
 */
@Service
public class DeckService {

    private final DeckRepository deckRepository;

    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    /**
     * Creates a new standard 52-card deck.
     */
    public Deck createDeck() {
        String deckId = UUID.randomUUID().toString();
        Deck deck = new Deck(deckId);
        return deckRepository.save(deck);
    }

    /**
     * Finds a deck by ID.
     */
    public Deck findDeckById(String deckId) {
        return deckRepository.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found: " + deckId));
    }

    /**
     * Checks if a deck exists.
     */
    public boolean deckExists(String deckId) {
        return deckRepository.existsById(deckId);
    }
}

