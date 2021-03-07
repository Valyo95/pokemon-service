package com.example.pokemon.controller;

import com.example.pokemon.dto.scheme.Pokemon;
import com.example.pokemon.service.PokeApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pokemon")
@Slf4j
public class PokemonController {

    private final Predicate<Pokemon> redVersionPokemons =
            pokemon -> pokemon.getGameIndices()
                    .stream()
                    .anyMatch(gameIndex ->
                            gameIndex.getVersion().getName().equals("red")
                                    && gameIndex.getVersion().getUrl().equals("https://pokeapi.co/api/v2/version/1/")
                    );

    private final PokeApiClient pokeApiClient;

    public PokemonController(PokeApiClient pokeApiClient) {
        this.pokeApiClient = pokeApiClient;
    }

    @GetMapping("/getTop5Highest")
    public List<Pokemon> getTop5Highest() {
        return getTopByComparatorAndFilter(pokeApiClient.getAllPokemons(),5, redVersionPokemons,
                Comparator.comparingInt(Pokemon::getHeight), true);
    }

    @GetMapping("/getTop5Heaviest")
    public List<Pokemon> getTop5Heaviest() {
        return getTopByComparatorAndFilter(pokeApiClient.getAllPokemons(), 5, redVersionPokemons,
                Comparator.comparingInt(Pokemon::getWeight),true);
    }

    @GetMapping("/getTop5ByBaseExperience")
    public List<Pokemon> getTop5ByBaseExperience() {
        return getTopByComparatorAndFilter(pokeApiClient.getAllPokemons(), 5, redVersionPokemons,
                Comparator.comparingInt(Pokemon::getBaseExperience),true);
    }

    private <T> List<T> getTopByComparator(List<T> list, int limit, Comparator<T> comparator, boolean reversed) {
        return getTopByComparatorAndFilter(list, limit, x -> true, comparator, reversed);
    }

    private <T> List<T> getTopByComparatorAndFilter(List<T> list, int limit, Predicate<T> filter,
                                                    Comparator<T> comparator, boolean reversed) {
        return list.stream()
                .filter(filter)
                .sorted(reversed ? comparator.reversed() : comparator)
                .limit(limit)
                .collect(Collectors.toList());
    }

}
