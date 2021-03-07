package com.example.pokemon.dto.scheme;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "back_female",
        "back_shiny_female",
        "back_default",
        "front_female",
        "front_shiny_female",
        "back_shiny",
        "front_default",
        "front_shiny"
})
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Sprites {

    @JsonProperty("back_female")
    private Object backFemale;
    @JsonProperty("back_shiny_female")
    private Object backShinyFemale;
    @JsonProperty("back_default")
    private String backDefault;
    @JsonProperty("front_female")
    private Object frontFemale;
    @JsonProperty("front_shiny_female")
    private Object frontShinyFemale;
    @JsonProperty("back_shiny")
    private String backShiny;
    @JsonProperty("front_default")
    private String frontDefault;
    @JsonProperty("front_shiny")
    private String frontShiny;

}
