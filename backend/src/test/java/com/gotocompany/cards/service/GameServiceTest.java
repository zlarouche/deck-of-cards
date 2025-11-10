package com.gotocompany.cards.service;

import com.gotocompany.cards.model.Card;
import com.gotocompany.cards.model.Game;
import com.gotocompany.cards.model.Player;
import com.gotocompany.cards.repository.DeckRepository;
import com.gotocompany.cards.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private GameService gameService;
    private DeckService deckService;
    private GameRepository gameRepository;
    private DeckRepository deckRepository;

    @BeforeEach
    void setUp() {
        gameRepository = new GameRepository();
        deckRepository = new DeckRepository();
        deckService = new DeckService(deckRepository);
        gameService = new GameService(gameRepository, deckRepository);
    }

    @Test
    void testCreateGame() {
        Game game = gameService.createGame();
        assertNotNull(game);
        assertNotNull(game.getId());
        assertEquals(0, game.getShoeSize());
        assertEquals(0, game.getPlayers().size());
        assertEquals(0, game.getAddedDeckIds().size());
    }

    @Test
    void testDeleteGame() {
        Game game = gameService.createGame();
        String gameId = game.getId();
        assertTrue(gameRepository.existsById(gameId));
        
        gameService.deleteGame(gameId);
        assertFalse(gameRepository.existsById(gameId));
    }

    @Test 
    void testGetGames() {
        Game game = gameService.createGame();
        List<Game> games = gameService.getGames();
        assertEquals(1, games.size());
        assertEquals(game.getId(), games.get(0).getId());
    }

    @Test
    void testGetGamesEmpty() {
        List<Game> games = gameService.getGames();
        assertEquals(0, games.size());
    }

    @Test
    void testDeleteNonExistentGame() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.deleteGame("non-existent");
        });
    }

    @Test
    void testAddDeckToGame() {
        Game game = gameService.createGame();
        var deck = deckService.createDeck();
        
        gameService.addDeckToGame(game.getId(), deck.getId());
        
        Game updatedGame = gameService.findGameById(game.getId());
        assertEquals(52, updatedGame.getShoeSize());
    }

    @Test
    void testAddMultipleDecksToGame() {
        Game game = gameService.createGame();
        var deck1 = deckService.createDeck();
        var deck2 = deckService.createDeck();
        
        gameService.addDeckToGame(game.getId(), deck1.getId());
        gameService.addDeckToGame(game.getId(), deck2.getId());
        
        Game updatedGame = gameService.findGameById(game.getId());
        assertEquals(104, updatedGame.getShoeSize());
    }

    @Test
    void testCannotAddSameDeckTwice() {
        Game game = gameService.createGame();
        var deck = deckService.createDeck();
        
        gameService.addDeckToGame(game.getId(), deck.getId());
        
        assertThrows(IllegalStateException.class, () -> {
            gameService.addDeckToGame(game.getId(), deck.getId());
        });
    }

    @Test
    void testAddPlayer() {
        Game game = gameService.createGame();
        gameService.addPlayer(game.getId(), "Alice");
        
        Game updatedGame = gameService.findGameById(game.getId());
        Player player = updatedGame.getPlayer("Alice");
        assertNotNull(player);
        assertEquals("Alice", player.getName());
    }

    @Test
    void testRemovePlayer() {
        Game game = gameService.createGame();
        gameService.addPlayer(game.getId(), "Alice");
        gameService.removePlayer(game.getId(), "Alice");
        
        Game updatedGame = gameService.findGameById(game.getId());
        assertNull(updatedGame.getPlayer("Alice"));
    }

    @Test
    void testDealCards() {
        Game game = gameService.createGame();
        var deck = deckService.createDeck();
        gameService.addDeckToGame(game.getId(), deck.getId());
        gameService.addPlayer(game.getId(), "Alice");
        
        List<Card> dealtCards = gameService.dealCards(game.getId(), "Alice", 5);
        
        assertEquals(5, dealtCards.size());
        Game updatedGame = gameService.findGameById(game.getId());
        assertEquals(47, updatedGame.getShoeSize());
        Player player = updatedGame.getPlayer("Alice");
        assertEquals(5, player.getHandSize());
    }

    @Test
    void testDealAllCardsFromSingleDeck() {
        Game game = gameService.createGame();
        var deck = deckService.createDeck();
        gameService.addDeckToGame(game.getId(), deck.getId());
        gameService.addPlayer(game.getId(), "Alice");
        
        // Shuffle first
        gameService.shuffleGameDeck(game.getId());
        
        // Deal all 52 cards
        for (int i = 0; i < 52; i++) {
            gameService.dealCards(game.getId(), "Alice", 1);
        }
        
        // 53rd call should fail
        assertThrows(IllegalStateException.class, () -> {
            gameService.dealCards(game.getId(), "Alice", 1);
        });
    }

    @Test
    void testGetPlayerCards() {
        Game game = gameService.createGame();
        var deck = deckService.createDeck();
        gameService.addDeckToGame(game.getId(), deck.getId());
        gameService.addPlayer(game.getId(), "Alice");
        gameService.dealCards(game.getId(), "Alice", 3);
        
        List<Card> playerCards = gameService.getPlayerCards(game.getId(), "Alice");
        assertEquals(3, playerCards.size());
    }

    @Test
    void testGetPlayersSorted() {
        Game game = gameService.createGame();
        var deck = deckService.createDeck();
        gameService.addDeckToGame(game.getId(), deck.getId());
        gameService.addPlayer(game.getId(), "Alice");
        gameService.addPlayer(game.getId(), "Bob");
        
        // Deal different numbers of cards
        gameService.dealCards(game.getId(), "Alice", 5);
        gameService.dealCards(game.getId(), "Bob", 3);
        
        List<Player> sortedPlayers = gameService.getPlayersSorted(game.getId());
        assertEquals(2, sortedPlayers.size());
        // Alice should be first as she has more cards (likely higher value)
        assertTrue(sortedPlayers.get(0).getHandValue() >= sortedPlayers.get(1).getHandValue());
    }

    @Test
    void testGetUndealtCardsBySuit() {
        Game game = gameService.createGame();
        var deck = deckService.createDeck();
        gameService.addDeckToGame(game.getId(), deck.getId());
        
        var suitCounts = gameService.getUndealtCardsBySuit(game.getId());
        
        // Each suit should have 13 cards in a standard deck
        suitCounts.values().forEach(count -> assertEquals(13, count));
    }

    @Test
    void testShuffleGameDeck() {
        Game game = gameService.createGame();
        var deck = deckService.createDeck();
        gameService.addDeckToGame(game.getId(), deck.getId());
        
        // Get initial order
        Game gameBefore = gameService.findGameById(game.getId());
        List<Card> beforeShuffle = gameBefore.getShoe();
        
        // Shuffle
        gameService.shuffleGameDeck(game.getId());
        
        // Get order after shuffle
        Game gameAfter = gameService.findGameById(game.getId());
        List<Card> afterShuffle = gameAfter.getShoe();
        
        // Size should be the same
        assertEquals(beforeShuffle.size(), afterShuffle.size());
        
        // Very likely the order should be different (though theoretically possible to be the same)
        // We'll check that at least some cards are in different positions
        boolean orderChanged = false;
        for (int i = 0; i < Math.min(beforeShuffle.size(), afterShuffle.size()); i++) {
            if (!beforeShuffle.get(i).equals(afterShuffle.get(i))) {
                orderChanged = true;
                break;
            }
        }
        // This is probabilistic, but with 52 cards, it's extremely unlikely to be in the same order
        assertTrue(orderChanged || beforeShuffle.size() <= 1);
    }
}

