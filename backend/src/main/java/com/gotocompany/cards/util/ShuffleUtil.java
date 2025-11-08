package com.gotocompany.cards.util;

import com.gotocompany.cards.model.Card;

import java.security.SecureRandom;
import java.util.List;

/**
 * Utility class for shuffling cards using the Fisher-Yates shuffle algorithm.
 * 
 * Fisher-Yates shuffle is an unbiased algorithm that produces a uniformly random
 * permutation of the input. It has O(n) time complexity where n is the number of elements.
 * 
 * We use SecureRandom instead of Random for better cryptographic-quality randomness,
 * which is important for card games where fairness is critical.
 * 
 * This implementation does not use library-provided shuffle operations as per requirements.
 */
public class ShuffleUtil {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Shuffles a list of cards in-place using the Fisher-Yates algorithm.
     * 
     * Algorithm:
     * 1. Start from the last element
     * 2. Pick a random element from the remaining unshuffled portion (including current)
     * 3. Swap it with the current element
     * 4. Move to the previous element and repeat
     * 
     * This ensures each permutation has equal probability (1/n!).
     * 
     * @param cards The list of cards to shuffle (modified in-place)
     */
    public static void shuffle(List<Card> cards) {
        if (cards == null || cards.isEmpty() || cards.size() == 1) {
            return; // Nothing to shuffle or already shuffled
        }

        // Fisher-Yates shuffle: iterate from end to beginning
        for (int i = cards.size() - 1; i > 0; i--) {
            // Pick a random index from 0 to i (inclusive)
            int j = random.nextInt(i + 1);
            
            // Swap cards at positions i and j
            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }
}

