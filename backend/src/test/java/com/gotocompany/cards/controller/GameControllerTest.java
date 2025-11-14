package com.gotocompany.cards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotocompany.cards.dto.AddDeckRequest;
import com.gotocompany.cards.dto.AddPlayerRequest;
import com.gotocompany.cards.dto.RemovePlayerRequest;
import com.gotocompany.cards.dto.DealCardsRequest;
import com.gotocompany.cards.model.Game;
import com.gotocompany.cards.model.Card;
import com.gotocompany.cards.model.enums.Suit;
import com.gotocompany.cards.model.enums.FaceValue;
import com.gotocompany.cards.service.DeckService;
import com.gotocompany.cards.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private DeckService deckService;

    @Autowired
    private ObjectMapper objectMapper;

    private String gameId;

    @BeforeEach
    void setUp() {
        gameId = "test-game-id";
    }

    @SuppressWarnings("null")
    @Test
    void testCreateGame() throws Exception {
        Game mockGame = new Game(gameId, "Test Game");
        when(gameService.createGame(anyString())).thenReturn(mockGame);
        
        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Game\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.gameId").value(gameId))
                .andExpect(jsonPath("$.name").value("Test Game"));
    }

    @SuppressWarnings("null")
    @Test
    void testAddDeckToGame() throws Exception {
        AddDeckRequest request = new AddDeckRequest("deck-id");
        doNothing().when(gameService).addDeckToGame(anyString(), anyString());
        
        mockMvc.perform(post("/api/games/" + gameId + "/decks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteGame() throws Exception {
        doNothing().when(gameService).deleteGame(anyString());

        mockMvc.perform(delete("/api/games/" + gameId))
                .andExpect(status().isNoContent());
    }

    @SuppressWarnings("null")
    @Test
    void testGetGames() throws Exception {
        List<Game> games = new ArrayList<>();
        games.add(new Game("game-1", "Test Game"));
        games.add(new Game("game-2", "Test Game 2"));
        when(gameService.getGames()).thenReturn(games);
        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(games.size()));
    }

    @SuppressWarnings("null")
    @Test
    void testGetDecks() throws Exception {
        Set<String> deckIds = new HashSet<>();
        deckIds.add("deck-1");
        deckIds.add("deck-2");
        when(gameService.getAddedDeckIds(anyString())).thenReturn(deckIds);
        mockMvc.perform(get("/api/games/" + gameId + "/decks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @SuppressWarnings("null")
    @Test
    void testAddPlayer() throws Exception {
        AddPlayerRequest request = new AddPlayerRequest("Alice");
        doNothing().when(gameService).addPlayer(anyString(), anyString());
        
        mockMvc.perform(post("/api/games/" + gameId + "/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @SuppressWarnings("null")
    @Test
    void testRemovePlayer() throws Exception {
        RemovePlayerRequest request = new RemovePlayerRequest("Alice");
        doNothing().when(gameService).removePlayer(anyString(), anyString());

        mockMvc.perform(delete("/api/games/" + gameId + "/players/Alice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @SuppressWarnings("null")
    @Test
    void testDealCards() throws Exception {
        DealCardsRequest request = new DealCardsRequest("Alice", 5);
        when(gameService.dealCards(anyString(), anyString(), anyInt())).thenReturn(new ArrayList<>());
        
        mockMvc.perform(post("/api/games/" + gameId + "/deal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @SuppressWarnings("null")
    @Test
    void testGetPlayerCards() throws Exception {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Suit.HEARTS, FaceValue.ACE));
        cards.add(new Card(Suit.DIAMONDS, FaceValue.KING));
        when(gameService.getPlayerCards(anyString(), anyString())).thenReturn(cards);
        mockMvc.perform(get("/api/games/" + gameId + "/players/Alice/cards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testShuffleGameDeck() throws Exception {
        doNothing().when(gameService).shuffleGameDeck(anyString());
        
        mockMvc.perform(post("/api/games/" + gameId + "/shuffle"))
                .andExpect(status().isOk());
    }

    @Test
    void testResetGame() throws Exception {
        doNothing().when(gameService).resetGame(anyString());

        mockMvc.perform(post("/api/games/" + gameId + "/reset"))
                .andExpect(status().isOk());
    }

    @SuppressWarnings("null")
    @Test
    void testGetPlayers() throws Exception {
        when(gameService.getPlayersSorted(anyString())).thenReturn(new ArrayList<>());
        
        mockMvc.perform(get("/api/games/" + gameId + "/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SuppressWarnings("null")
    @Test
    void testGetUndealtCardsBySuit() throws Exception {
        when(gameService.getUndealtCardsBySuit(anyString())).thenReturn(new HashMap<>());
        
        mockMvc.perform(get("/api/games/" + gameId + "/undealt/suits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @SuppressWarnings("null")
    @Test
    void testGetUndealtCardsCount() throws Exception {
        when(gameService.getUndealtCardsCount(anyString())).thenReturn(new HashMap<>());
        mockMvc.perform(get("/api/games/" + gameId + "/undealt/cards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
