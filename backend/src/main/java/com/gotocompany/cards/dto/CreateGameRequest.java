package com.gotocompany.cards.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateGameRequest {

    @NotBlank(message = "Game name must not be blank")
    private String name;

    public CreateGameRequest() {
    }

    public CreateGameRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


