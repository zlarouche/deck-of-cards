package com.gotocompany.cards.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testConstructorWithValidParameters() {
        Game game = new Game("game-1", "Test Game");
        assertNotNull(game);
        assertEquals("game-1", game.getId());
        assertEquals("Test Game", game.getName());
    }

    @Test
    void testConstructorWithNullIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Game(null, "Test Game"));
    }

    @Test
    void testConstructorWithBlankIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Game("  ", "Test Game"));
        assertThrows(IllegalArgumentException.class, () -> new Game("", "Test Game"));
    }

    @Test
    void testConstructorWithNullNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Game("game-1", null));
    }

    @Test
    void testConstructorWithBlankNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Game("game-1", "  "));
        assertThrows(IllegalArgumentException.class, () -> new Game("game-1", ""));
    }

    @Test
    void testAddDeck() {
        Game game = new Game("game-1", "Test Game");
        Deck deck = new Deck("1");
        game.addDeck(deck);
        assertEquals(52, game.getShoeSize());
    }
    
    @Test
    void testAddDeckNullThrows() {
        Game game = new Game("game-1", "Test Game");
        Deck deck = null;
        assertThrows(IllegalArgumentException.class, () -> game.addDeck(deck));
    }

    @Test
    void testAddDeckAlreadyAddedThrows() {
        Game game = new Game("game-1", "Test Game");
        Deck deck = new Deck("1");
        game.addDeck(deck);
        assertThrows(IllegalStateException.class, () -> game.addDeck(deck));
    }

    @Test
    void testAddPlayerNullThrows() {
        Game game = new Game("game-1", "Test Game");
        Player player = null;
        assertThrows(IllegalArgumentException.class, () -> game.addPlayer(player));
    }

    @Test
    void testRemovePlayerNullThrows() {
        Game game = new Game("game-1", "Test Game");
        assertThrows(IllegalArgumentException.class, () -> game.removePlayer(null));
    }
}

