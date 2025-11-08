package com.gotocompany.cards.util;

import com.gotocompany.cards.model.Card;
import com.gotocompany.cards.model.enums.FaceValue;
import com.gotocompany.cards.model.enums.Suit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShuffleUtilTest {

    @Test
    void testShuffleEmptyList() {
        List<Card> cards = new ArrayList<>();
        ShuffleUtil.shuffle(cards);
        assertTrue(cards.isEmpty());
    }

    @Test
    void testShuffleSingleCard() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Suit.HEARTS, FaceValue.ACE));
        ShuffleUtil.shuffle(cards);
        assertEquals(1, cards.size());
        assertEquals(Suit.HEARTS, cards.get(0).getSuit());
        assertEquals(FaceValue.ACE, cards.get(0).getFaceValue());
    }

    @Test
    void testShuffleMaintainsAllCards() {
        List<Card> cards = createStandardDeck();
        Set<Card> originalSet = new HashSet<>(cards);
        
        ShuffleUtil.shuffle(cards);
        Set<Card> shuffledSet = new HashSet<>(cards);
        
        assertEquals(originalSet.size(), shuffledSet.size());
        assertEquals(originalSet, shuffledSet); // Same cards, possibly different order
    }

    @Test
    void testShuffleChangesOrder() {
        List<Card> cards = createStandardDeck();
        List<Card> originalOrder = new ArrayList<>(cards);
        
        // Shuffle multiple times to increase likelihood of order change
        boolean orderChanged = false;
        for (int i = 0; i < 10; i++) {
            ShuffleUtil.shuffle(cards);
            if (!cards.equals(originalOrder)) {
                orderChanged = true;
                break;
            }
        }
        
        // With 52 cards, it's extremely unlikely to get the same order
        assertTrue(orderChanged || cards.size() <= 1);
    }

    @Test
    void testShuffleIsDeterministicInSize() {
        List<Card> cards = createStandardDeck();
        int originalSize = cards.size();
        
        ShuffleUtil.shuffle(cards);
        
        assertEquals(originalSize, cards.size());
    }

    private List<Card> createStandardDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (FaceValue faceValue : FaceValue.values()) {
                deck.add(new Card(suit, faceValue));
            }
        }
        return deck;
    }
}

