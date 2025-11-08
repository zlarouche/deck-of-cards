package com.gotocompany.cards.repository;

import com.gotocompany.cards.model.Game;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository for managing games in-memory.
 * Uses ConcurrentHashMap for thread-safe operations.
 */
@Repository
public class GameRepository {

    private final Map<String, Game> games = new ConcurrentHashMap<>();

    /**
     * Saves a game to the repository.
     */
    public Game save(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        games.put(game.getId(), game);
        return game;
    }

    /**
     * Finds a game by ID.
     */
    public Optional<Game> findById(String id) {
        return Optional.ofNullable(games.get(id));
    }

    /**
     * Checks if a game exists.
     */
    public boolean existsById(String id) {
        return games.containsKey(id);
    }

    /**
     * Deletes a game by ID.
     */
    public void deleteById(String id) {
        games.remove(id);
    }

    /**
     * Returns all games.
     */
    public Map<String, Game> findAll() {
        return new ConcurrentHashMap<>(games);
    }
}

