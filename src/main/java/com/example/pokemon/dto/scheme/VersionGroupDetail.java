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
        "move_learn_method",
        "level_learned_at",
        "version_group"
})
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VersionGroupDetail {

    @JsonProperty("move_learn_method")
    private MoveLearnMethod moveLearnMethod;
    @JsonProperty("level_learned_at")
    private Integer levelLearnedAt;
    @JsonProperty("version_group")
    private VersionGroup versionGroup;

}
