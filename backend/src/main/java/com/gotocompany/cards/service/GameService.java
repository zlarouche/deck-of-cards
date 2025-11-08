package com.gotocompany.cards.service;

import com.gotocompany.cards.model.Card;
import com.gotocompany.cards.model.Deck;
import com.gotocompany.cards.model.Game;
import com.gotocompany.cards.model.Player;
import com.gotocompany.cards.model.enums.FaceValue;
import com.gotocompany.cards.model.enums.Suit;
import com.gotocompany.cards.repository.DeckRepository;
import com.gotocompany.cards.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing games and game operations.
 */
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final DeckRepository deckRepository;

    public GameService(GameRepository gameRepository, DeckRepository deckRepository) {
        this.gameRepository = gameRepository;
        this.deckRepository = deckRepository;
    }

    /**
     * Creates a new game.
     */
    public Game createGame() {
        String gameId = UUID.randomUUID().toString();
        Game game = new Game(gameId);
        return gameRepository.save(game);
    }

    /**
     * Deletes a game and related decks.
     */
    public void deleteGame(String gameId) {
        Game game = findGameById(gameId);
        for (String deckId : game.getAddedDeckIds()) {
            deckRepository.findById(deckId).ifPresent(deck -> {
                if (deck.isAdded()) {
                    deckRepository.deleteById(deckId);
                }
            });
        }
        gameRepository.deleteById(gameId);
    }

    /**
     * Finds a game by ID.
     */
    public Game findGameById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));
    }

    /**
     * Adds a deck to the game's shoe.
     * Once added, a deck cannot be removed.
     */
    public void addDeckToGame(String gameId, String deckId) {
        Game game = findGameById(gameId);
        com.gotocompany.cards.model.Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found: " + deckId));

        if (game.isDeckAdded(deckId)) {
            throw new IllegalStateException("Deck " + deckId + " has already been added to the game");
        }

        deck.setAdded(true);
        game.addDeck(deck);
        gameRepository.save(game);
    }

    /**
     * Gets the IDs of all decks added to the game.
     */
    public Set<String> getAddedDeckIds(String gameId) {
        Game game = findGameById(gameId);
        return game.getAddedDeckIds();
    }

    /**
     * Adds a player to the game.
     */
    public void addPlayer(String gameId, String playerName) {
        Game game = findGameById(gameId);
        Player player = new Player(playerName);
        game.addPlayer(player);
        gameRepository.save(game);
    }

    /**
     * Removes a player from the game.
     */
    public void removePlayer(String gameId, String playerName) {
        Game game = findGameById(gameId);
        if (game.getPlayer(playerName) == null) {
            throw new IllegalArgumentException("Player not found: " + playerName);
        }
        game.removePlayer(playerName);
        gameRepository.save(game);
    }

    /**
     * Deals cards to a player from the game deck.
     */
    public List<Card> dealCards(String gameId, String playerName, int count) {
        Game game = findGameById(gameId);
        return game.dealCards(playerName, count);
    }

    /**
     * Gets the list of cards for a player.
     */
    public List<Card> getPlayerCards(String gameId, String playerName) {
        Game game = findGameById(gameId);
        Player player = game.getPlayer(playerName);
        if (player == null) {
            throw new IllegalArgumentException("Player not found: " + playerName);
        }
        return player.getHand();
    }

    /**
     * Gets the list of players sorted by hand value (descending).
     */
    public List<Player> getPlayersSorted(String gameId) {
        Game game = findGameById(gameId);
        return game.getPlayers().stream()
                .sorted(Comparator.comparing(Player::getHandValue).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Gets the count of undealt cards per suit.
     */
    public Map<Suit, Integer> getUndealtCardsBySuit(String gameId) {
        Game game = findGameById(gameId);
        List<Card> shoe = game.getShoe();
        
        Map<Suit, Integer> suitCounts = new HashMap<>();
        for (Suit suit : Suit.values()) {
            suitCounts.put(suit, 0);
        }
        
        for (Card card : shoe) {
            suitCounts.put(card.getSuit(), suitCounts.get(card.getSuit()) + 1);
        }
        
        return suitCounts;
    }

    /**
     * Gets the count of each card (suit and value) remaining in the game deck.
     * Sorted by suit (hearts, spades, clubs, diamonds) and face value from high to low
     * (King, Queen, Jack, 10...2, Ace).
     */
    public Map<Suit, Map<FaceValue, Integer>> getUndealtCardsCount(String gameId) {
        Game game = findGameById(gameId);
        List<Card> shoe = game.getShoe();
        
        // Initialize map with all suits and face values
        Map<Suit, Map<FaceValue, Integer>> cardCounts = new LinkedHashMap<>();
        for (Suit suit : Suit.values()) {
            Map<FaceValue, Integer> faceValueCounts = new LinkedHashMap<>();
            // Order: King, Queen, Jack, 10, 9, 8, 7, 6, 5, 4, 3, 2, Ace
            List<FaceValue> orderedValues = Arrays.asList(
                    FaceValue.KING, FaceValue.QUEEN, FaceValue.JACK,
                    FaceValue.TEN, FaceValue.NINE, FaceValue.EIGHT,
                    FaceValue.SEVEN, FaceValue.SIX, FaceValue.FIVE,
                    FaceValue.FOUR, FaceValue.THREE, FaceValue.TWO,
                    FaceValue.ACE
            );
            for (FaceValue faceValue : orderedValues) {
                faceValueCounts.put(faceValue, 0);
            }
            cardCounts.put(suit, faceValueCounts);
        }
        
        // Count cards in shoe
        for (Card card : shoe) {
            Map<FaceValue, Integer> faceValueCounts = cardCounts.get(card.getSuit());
            faceValueCounts.put(card.getFaceValue(), faceValueCounts.get(card.getFaceValue()) + 1);
        }
        
        return cardCounts;
    }

    /**
     * Shuffles the game deck (shoe).
     */
    public void shuffleGameDeck(String gameId) {
        Game game = findGameById(gameId);
        game.shuffle();
        // No need to save as shuffle modifies the internal list in-place
    }
}

