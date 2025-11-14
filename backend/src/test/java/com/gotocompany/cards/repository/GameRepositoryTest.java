package com.gotocompany.cards.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GameRepositoryTest {
    
    @Test
    void testSaveGameNullThrows() {
        GameRepository gameRepository = new GameRepository();
        assertThrows(IllegalArgumentException.class, () -> gameRepository.save(null));
    }
}
