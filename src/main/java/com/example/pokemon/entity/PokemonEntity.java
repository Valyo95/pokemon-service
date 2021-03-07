package com.example.pokemon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PokemonEntity implements Serializable {
    @Id
    private Integer id;

    private String name;
    private String url;

    private Integer weight;
    private Integer height;
    private Integer baseExperience;

    private String locationAreaEncounters;

    private Boolean isDefault;

    @Column(name = "order_number")
    private Integer order;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "url", insertable = false, updatable = false),
            @JoinColumn(name = "name", insertable = false, updatable = false)
    })
    private List<GameIndexEntity> gameIndex;

}
