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
        "stat",
        "effort",
        "base_stat"
})
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Stat {

    @JsonProperty("stat")
    private Stat_ stat;
    @JsonProperty("effort")
    private Integer effort;
    @JsonProperty("base_stat")
    private Integer baseStat;

}
