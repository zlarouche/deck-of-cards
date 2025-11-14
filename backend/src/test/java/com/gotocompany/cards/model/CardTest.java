package com.gotocompany.cards.model;

import com.gotocompany.cards.model.enums.FaceValue;
import com.gotocompany.cards.model.enums.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testConstructorWithValidParameters() {
        Card card = new Card(Suit.HEARTS, FaceValue.ACE);
        assertNotNull(card);
        assertEquals(Suit.HEARTS, card.getSuit());
        assertEquals(FaceValue.ACE, card.getFaceValue());
    }

    @Test
    void testConstructorWithNullSuitThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Card(null, FaceValue.ACE));
    }

    @Test
    void testConstructorWithNullFaceValueThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Card(Suit.HEARTS, null));
    }

    @Test
    void testConstructorWithBothNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Card(null, null));
    }
}

