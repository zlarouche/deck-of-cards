package com.gotocompany.cards.model;

import org.junit.jupiter.api.Test;

import com.gotocompany.cards.model.enums.FaceValue;
import com.gotocompany.cards.model.enums.Suit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class PlayerTest {

    @Test
    void testConstructorWithValidName() {
        Player player = new Player("Alice");
        assertNotNull(player);
        assertEquals("Alice", player.getName());
        assertEquals(0, player.getHandSize());
    }

    @Test
    void testConstructorWithNullNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Player(null));
    }

    @Test
    void testConstructorWithEmptyNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Player(""));
    }

    @Test
    void testConstructorWithBlankNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Player("  "));
        assertThrows(IllegalArgumentException.class, () -> new Player("\t"));
    }

    @Test
    void testAddCardNullThrows() {
        Player player = new Player("Alice");
        assertThrows(IllegalArgumentException.class, () -> player.addCard(null));
    }

    @Test
    void testAddCardsNullThrows() {
        Player player = new Player("Alice");
        assertThrows(IllegalArgumentException.class, () -> player.addCards(null));
    }
    
    @Test
    void testAddCard(){
        Player player = new Player("Alice");
        Card card = new Card(Suit.HEARTS, FaceValue.ACE);
        player.addCard(card);
        assertEquals(1, player.getHandSize());
        assertEquals(card, player.getHand().get(0));
    }

    @Test
    void testAddCards(){
        Player player = new Player("Alice");
        Card card1 = new Card(Suit.HEARTS, FaceValue.ACE);
        Card card2 = new Card(Suit.DIAMONDS, FaceValue.KING);
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        player.addCards(cards);
        assertEquals(2, player.getHandSize());
        assertEquals(card1, player.getHand().get(0));
        assertEquals(card2, player.getHand().get(1));
    }
    
    
}

