package com.example.pokemon.entity;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class VersionId implements Serializable {
    private String url;
    private String name;
}
