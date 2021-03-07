package com.example.pokemon.repository;

import com.example.pokemon.entity.GameIndexEntity;
import com.example.pokemon.entity.PokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<PokemonEntity, Integer> {
    List<PokemonEntity> findByGameIndexEquals(GameIndexEntity gameIndex);

    List<PokemonEntity> findTop5ByGameIndexEqualsOrderByHeightDesc(GameIndexEntity gameIndex);

    List<PokemonEntity> findTop5ByGameIndexEqualsOrderByWeightDesc(GameIndexEntity gameIndex);

    List<PokemonEntity> findTop5ByGameIndexEqualsOrderByBaseExperienceDesc(GameIndexEntity gameIndex);
}
