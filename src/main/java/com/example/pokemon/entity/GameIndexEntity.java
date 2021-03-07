package com.example.pokemon.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@IdClass(VersionId.class)
public class GameIndexEntity {
    @Id
    @Column(name = "url")
    private String url;
    @Id
    @Column(name = "name")
    private String name;

    private int gameIndex;
}
