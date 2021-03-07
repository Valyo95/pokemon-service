package com.example.pokemon.controller;

import com.example.pokemon.entity.PokemonEntity;
import com.example.pokemon.service.PokemonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pokemon_db")
@Slf4j
public class PokemonDBController {

    private final PokemonService pokemonService;

    public PokemonDBController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/initialize")
    public String initialize() {
        return pokemonService.initializeDB();
    }

    @GetMapping("/getAll")
    public List<PokemonEntity> getAll() {
        return pokemonService.getAll();
    }

    @GetMapping("/getTop5HighestRedVersioned")
    public List<PokemonEntity> getTop5HighestRedVersioned() {
        return pokemonService.findTop5ByGameIndexEqualsOrderByHeightDesc();
    }

    @GetMapping("/getTop5HeaviestRedVersioned")
    public List<PokemonEntity> getTop5HeaviestRedVersioned() {
        return pokemonService.findTop5ByGameIndexEqualsOrderByWeightDesc();
    }

    @GetMapping("/getTop5BaseExperiencedRedVersioned")
    public List<PokemonEntity> getTop5BaseExperiencedRedVersioned() {
        return pokemonService.findTop5ByGameIndexEqualsOrderByBaseExperienceDesc();
    }

//    @GetMapping("/getRedVersioned")
//    public List<PokemonEntity> getRedVersioned() {
//        return pokemonService.findByGameIndex();
//    }

}
