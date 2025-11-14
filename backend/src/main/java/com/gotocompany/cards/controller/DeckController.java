package com.gotocompany.cards.controller;

import com.gotocompany.cards.dto.CreateDeckResponse;
import com.gotocompany.cards.service.DeckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for deck operations.
 * Errors are handled centrally by GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/decks")
@Tag(name = "Decks", description = "API for managing card decks")
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @PostMapping
    @Operation(summary = "Create a new deck", description = "Creates a standard 52-card deck")
    public ResponseEntity<CreateDeckResponse> createDeck() {
        var deck = deckService.createDeck();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateDeckResponse(deck.getId()));
    }

    @GetMapping("/unassigned")
    @Operation(summary = "Get unassigned decks", description = "Returns IDs of decks that are not currently added to any game")
    public ResponseEntity<List<String>> getUnassignedDeckIds() {
        var deckIds = deckService.getUnassignedDeckIds();
        return ResponseEntity.ok(deckIds);
    }
}

