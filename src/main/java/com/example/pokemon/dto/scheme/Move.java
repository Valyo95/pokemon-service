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
        "version_group_details",
        "move"
})
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Move {

    @JsonProperty("version_group_details")
    private List<VersionGroupDetail> versionGroupDetails = null;
    @JsonProperty("move")
    private Move_ move;

}
