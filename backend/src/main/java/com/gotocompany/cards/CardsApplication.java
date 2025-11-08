package com.gotocompany.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Deck of Cards Game REST API.
 * 
 * This Spring Boot application provides REST endpoints for managing
 * card games, decks, players, and card dealing operations.
 */
@SpringBootApplication
public class CardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardsApplication.class, args);
    }
}

