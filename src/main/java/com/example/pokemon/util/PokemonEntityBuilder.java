package com.example.pokemon.util;

import com.example.pokemon.entity.GameIndexEntity;
import com.example.pokemon.entity.PokemonEntity;

import java.util.List;

public class PokemonEntityBuilder {
    private Integer id;

    private String name;
    private String url;

    private Integer weight;
    private Integer height;
    private Integer baseExperience;

    private String locationAreaEncounters;

    private Integer order;
    private Boolean isDefault;

    private List<GameIndexEntity> gameIndex;

    public PokemonEntityBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public PokemonEntityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PokemonEntityBuilder withHeight(Integer height) {
        this.height = height;
        return this;
    }

    public PokemonEntityBuilder withWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public PokemonEntityBuilder withLocationAreaEncounters(String locationAreaEncounters) {
        this.locationAreaEncounters = locationAreaEncounters;
        return this;
    }

    public PokemonEntityBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public PokemonEntityBuilder withIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    public PokemonEntityBuilder withBaseExperience(Integer baseExperience) {
        this.baseExperience = baseExperience;
        return this;
    }

    public PokemonEntityBuilder withOrder(Integer order) {
        this.order = order;
        return this;
    }

    public PokemonEntityBuilder withGameIndex(List<GameIndexEntity> gameIndex) {
        this.gameIndex = gameIndex;
        return this;
    }

    public PokemonEntity build() {
        return new PokemonEntity(id, name, url, weight, height, baseExperience, locationAreaEncounters, isDefault, order, gameIndex);
    }
}
