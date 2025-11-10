package com.gotocompany.cards.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.gotocompany.cards.model.Card;
import com.gotocompany.cards.model.Game;
import com.gotocompany.cards.model.Player;

/**
 * DTO for player representation in API responses.
 */
public class GameDto {
    private String id;
    private String name;
    private List<Card> shoe;
    private Map<String, Player> players;
    private Set<String> addedDeckIds;
    private int shoeSize;
    private int playerCount;

    public GameDto(Game game) {
        this.id = game.getId();
        this.name = game.getName();
        this.shoe = game.getShoe();
        this.players = game.getPlayers().stream()
                .collect(Collectors.toMap(Player::getName, Function.identity()));
        this.addedDeckIds = game.getAddedDeckIds();
        this.shoeSize = game.getShoeSize();
        this.playerCount = game.getPlayers().size();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Card> getShoe() {
        return shoe;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Set<String> getAddedDeckIds() {
        return addedDeckIds;
    }

    public int getShoeSize() {
        return shoeSize;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShoe(List<Card> shoe) {
        this.shoe = shoe;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public void setAddedDeckIds(Set<String> addedDeckIds) {
        this.addedDeckIds = addedDeckIds;
    }

    public void setShoeSize(int shoeSize) {
        this.shoeSize = shoeSize;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}
