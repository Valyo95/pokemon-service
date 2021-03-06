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
        "url",
        "name"
})
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Type_ {

    @JsonProperty("url")
    private String url;
    @JsonProperty("name")
    private String name;

}
