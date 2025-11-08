package com.gotocompany.cards.service;

import com.gotocompany.cards.model.Deck;
import com.gotocompany.cards.repository.DeckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckServiceTest {

    private DeckService deckService;
    private DeckRepository deckRepository;

    @BeforeEach
    void setUp() {
        deckRepository = new DeckRepository();
        deckService = new DeckService(deckRepository);
    }

    @Test
    void testCreateDeck() {
        Deck deck = deckService.createDeck();
        assertNotNull(deck);
        assertNotNull(deck.getId());
        assertEquals(52, deck.size());
    }

    @Test
    void testDeckHasAllCards() {
        Deck deck = deckService.createDeck();
        var cards = deck.getCards();
        
        assertEquals(52, cards.size());
        // Verify we have all combinations of suits and face values
        long uniqueCards = cards.stream().distinct().count();
        assertEquals(52, uniqueCards);
    }

    @Test
    void testFindDeckById() {
        Deck deck = deckService.createDeck();
        String deckId = deck.getId();
        
        Deck foundDeck = deckService.findDeckById(deckId);
        assertNotNull(foundDeck);
        assertEquals(deckId, foundDeck.getId());
    }

    @Test
    void testFindNonExistentDeck() {
        assertThrows(IllegalArgumentException.class, () -> {
            deckService.findDeckById("non-existent");
        });
    }
}

