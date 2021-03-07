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
        "version",
        "game_index"
})
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GameIndex {

    @JsonProperty("version")
    private Version version;
    @JsonProperty("game_index")
    private Integer gameIndex;

}
