package com.gotocompany.cards.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.gotocompany.cards.model.Deck;

public class DeckRepositoryTest {
    
    @Test
    void testSaveDeckNullThrows() {
        DeckRepository deckRepository = new DeckRepository();
        assertThrows(IllegalArgumentException.class, () -> deckRepository.save(null));
    }

    @Test
    void testFindAll() {
        DeckRepository deckRepository = new DeckRepository();
        Deck deck1 = new Deck("deck-1");
        Deck deck2 = new Deck("deck-2");
        deckRepository.save(deck1);
        deckRepository.save(deck2);
        Map<String, Deck> allDecks = deckRepository.findAll();
        assertEquals(2, allDecks.size());
        assertTrue(allDecks.containsKey(deck1.getId()));
        assertTrue(allDecks.containsKey(deck2.getId()));
    }
}
