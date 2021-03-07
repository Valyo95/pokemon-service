package com.example.pokemon.dto.scheme;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "height",
        "weight",
        "base_experience",
        "location_area_encounters",
        "is_default",
        "order",
        "species",
        "types",
        "forms",
        "abilities",
        "stats",
        "moves",
        "sprites",
        "held_items",
        "game_indices"
})
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Pokemon {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("weight")
    private Integer weight;

    @JsonProperty("base_experience")
    private Integer baseExperience;

    @JsonProperty("location_area_encounters")
    private String locationAreaEncounters;

    @JsonProperty("is_default")
    private Boolean isDefault;

    @JsonProperty("order")
    private Integer order;

    @JsonProperty("species")
    private Species species;

    @JsonProperty("types")
    private List<Type> types = null;

    @JsonProperty("forms")
    private List<Form> forms = null;

    @JsonProperty("abilities")
    private List<Ability> abilities = null;

    @JsonProperty("stats")
    private List<Stat> stats = null;

    @JsonProperty("moves")
    private List<Move> moves = null;

    @JsonProperty("sprites")
    private Sprites sprites;

    @JsonProperty("held_items")
    private List<Object> heldItems = null;

    @JsonProperty("game_indices")
    private List<GameIndex> gameIndices = null;
}
