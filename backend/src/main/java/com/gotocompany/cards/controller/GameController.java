package com.gotocompany.cards.controller;

import com.gotocompany.cards.dto.*;
import com.gotocompany.cards.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for game operations.
 */
@RestController
@RequestMapping("/api/games")
@Tag(name = "Games", description = "API for managing card games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @Operation(summary = "Create a new game", description = "Creates a new game with an empty shoe and no players")
    public ResponseEntity<CreateGameResponse> createGame(@Valid @RequestBody CreateGameRequest request) {
        var game = gameService.createGame(request.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateGameResponse(game.getId(), game.getName()));
    }

    @DeleteMapping("/{gameId}")
    @Operation(summary = "Delete a game", description = "Deletes a game by its ID")
    public ResponseEntity<Void> deleteGame(@PathVariable String gameId) {
        gameService.deleteGame(gameId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all current games", description = "Returns a list of all current games")
    public ResponseEntity<List<GameDto>> getGames(){
        var games = gameService.getGames();
        List<GameDto> gameDtos = games.stream()
                .map(game -> new GameDto(game))
                .collect(Collectors.toList());
        return ResponseEntity.ok(gameDtos);
    }

    @PostMapping("/{gameId}/decks")
    @Operation(summary = "Add a deck to game", description = "Adds a deck to the game's shoe. Once added, a deck cannot be removed.")
    public ResponseEntity<Void> addDeckToGame(
            @PathVariable String gameId,
            @Valid @RequestBody AddDeckRequest request) {
        gameService.addDeckToGame(gameId, request.getDeckId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{gameId}/decks")
    @Operation(summary = "Get added decks for a game", description = "Returns the IDs of all decks added to the game")
    public ResponseEntity<Set<String>> getDecks(@PathVariable String gameId) {
        var deckIds = gameService.getAddedDeckIds(gameId);
        return ResponseEntity.ok(deckIds);
    }

    @PostMapping("/{gameId}/players")
    @Operation(summary = "Add a player to game", description = "Adds a player to the game")
    public ResponseEntity<Void> addPlayer(
            @PathVariable String gameId,
            @Valid @RequestBody AddPlayerRequest request) {
        gameService.addPlayer(gameId, request.getPlayerName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{gameId}/players/{playerName}")
    @Operation(summary = "Remove a player from game", description = "Removes a player from the game")
    public ResponseEntity<Void> removePlayer(
            @PathVariable String gameId,
            @PathVariable String playerName) {
        gameService.removePlayer(gameId, playerName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{gameId}/deal")
    @Operation(summary = "Deal cards to a player", description = "Deals the specified number of cards from the game deck to a player")
    public ResponseEntity<List<CardDto>> dealCards(
            @PathVariable String gameId,
            @Valid @RequestBody DealCardsRequest request) {
        var cards = gameService.dealCards(gameId, request.getPlayerName(), request.getCount());
        List<CardDto> cardDtos = cards.stream()
                .map(CardDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cardDtos);
    }

    @GetMapping("/{gameId}/players/{playerName}/cards")
    @Operation(summary = "Get player's cards", description = "Returns the list of cards held by a player")
    public ResponseEntity<List<CardDto>> getPlayerCards(
            @PathVariable String gameId,
            @PathVariable String playerName) {
        var cards = gameService.getPlayerCards(gameId, playerName);
        List<CardDto> cardDtos = cards.stream()
                .map(CardDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cardDtos);
    }

    @GetMapping("/{gameId}/players")
    @Operation(summary = "Get players sorted by hand value", description = "Returns all players sorted by total hand value in descending order")
    public ResponseEntity<List<PlayerDto>> getPlayersSorted(
            @PathVariable String gameId) {
        var players = gameService.getPlayersSorted(gameId);
        List<PlayerDto> playerDtos = players.stream()
                .map(PlayerDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(playerDtos);
    }

    @GetMapping("/{gameId}/undealt/suits")
    @Operation(summary = "Get undealt cards by suit", description = "Returns the count of undealt cards grouped by suit")
    public ResponseEntity<UndealtCardsBySuitDto> getUndealtCardsBySuit(
            @PathVariable String gameId) {
        var suitCounts = gameService.getUndealtCardsBySuit(gameId);
        return ResponseEntity.ok(new UndealtCardsBySuitDto(suitCounts));
    }

    @GetMapping("/{gameId}/undealt/cards")
    @Operation(summary = "Get undealt cards count", description = "Returns the count of each card remaining, sorted by suit and face value")
    public ResponseEntity<UndealtCardsCountDto> getUndealtCardsCount(
            @PathVariable String gameId) {
        var cardCounts = gameService.getUndealtCardsCount(gameId);
        return ResponseEntity.ok(new UndealtCardsCountDto(cardCounts));
    }

    @PostMapping("/{gameId}/shuffle")
    @Operation(summary = "Shuffle game deck", description = "Shuffles the game deck (shoe) using Fisher-Yates algorithm")
    public ResponseEntity<Void> shuffleGameDeck(@PathVariable String gameId) {
        gameService.shuffleGameDeck(gameId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{gameId}/reset")
    @Operation(summary = "Reset game", description = "Returns all dealt cards to the shoe while keeping players in the game")
    public ResponseEntity<Void> resetGame(@PathVariable String gameId) {
        gameService.resetGame(gameId);
        return ResponseEntity.ok().build();
    }

}

