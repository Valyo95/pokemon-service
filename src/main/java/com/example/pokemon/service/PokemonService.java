package com.example.pokemon.service;

import com.example.pokemon.dto.scheme.Pokemon;
import com.example.pokemon.entity.GameIndexEntity;
import com.example.pokemon.entity.PokemonEntity;
import com.example.pokemon.repository.PokemonRepository;
import com.example.pokemon.util.PokemonEntityBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PokemonService {
    private final String POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/";

    @Value("${pokemon.initializeDB:false}")
    private boolean shouldInitializeDBOnStart;

    private boolean databaseInitialized = false;

    private final PokemonRepository pokemonRepository;
    private final GameIndexService gameIndexService;
    private final PokeApiClient pokeApiClient;

    public PokemonService(PokemonRepository pokemonRepository, GameIndexService gameIndexService, PokeApiClient pokeApiClient) {
        this.pokemonRepository = pokemonRepository;
        this.gameIndexService = gameIndexService;
        this.pokeApiClient = pokeApiClient;
    }

    @PostConstruct
    private void initialize() {
        if(shouldInitializeDBOnStart) {
            initializeDB();
        } else {
            log.info("Database not initialized. " +
                    "In order to use the PokemonDBController please call http://localhost:8080/pokemon_db/initialize");
        }
    }

    public String initializeDB() {
        if(databaseInitialized == false) {
            log.info("Initializing the pokemon db");
            List<Pokemon> pokemons = pokeApiClient.getAllPokemons();
            List<PokemonEntity> pokemonEntities = convertPokemonsToEntities(pokemons);
            insertPokemons(pokemonEntities);
            databaseInitialized = true;
            return "Database initialized!";
        }
        return "Database already initialized!";
    }

    public List<PokemonEntity> getAll() {
        log.info("Fetching all pokemon entities");
        return pokemonRepository.findAll();
    }

    public PokemonEntity insertPokemon(Pokemon pokemon) {
        PokemonEntity pokemonEntity = new PokemonEntityBuilder()
                .withId(pokemon.getId())
                .withName(pokemon.getName())
                .withHeight(pokemon.getHeight())
                .withWeight(pokemon.getWeight())
                .withLocationAreaEncounters(pokemon.getLocationAreaEncounters())
                .withBaseExperience(pokemon.getBaseExperience())
                .withIsDefault(pokemon.getIsDefault())
                .withOrder(pokemon.getOrder())
                .build();

        log.info("Inserting pokemon: {}", pokemonEntity);
        return pokemonRepository.save(pokemonEntity);
    }

    @Transactional
    public List<PokemonEntity> insertPokemons(List<PokemonEntity> pokemonEntities) {
        List<GameIndexEntity> gameIndexEntities = pokemonEntities.stream().flatMap(x -> x.getGameIndex().stream()).distinct()
                .collect(Collectors.toList());
        gameIndexService.insertGameIndecies(gameIndexEntities);

        log.info("Started inserting pokemons");
        pokemonEntities = pokemonRepository.saveAll(pokemonEntities);
        log.info("Pokemon entities inserted into DB");
        return pokemonEntities;
    }

    public List<PokemonEntity> findByGameIndex() {
        GameIndexEntity gameIndexEntity = gameIndexService.findByGameIndexUrlAndVersion("https://pokeapi.co/api/v2/version/1/", "red");
        return pokemonRepository.findByGameIndexEquals(gameIndexEntity);
    }

    public List<PokemonEntity> findTop5ByGameIndexEqualsOrderByHeightDesc() {
        GameIndexEntity gameIndexEntity = gameIndexService.findByGameIndexUrlAndVersion("https://pokeapi.co/api/v2/version/1/", "red");
        return pokemonRepository.findTop5ByGameIndexEqualsOrderByHeightDesc(gameIndexEntity);
    }

    public List<PokemonEntity> findTop5ByGameIndexEqualsOrderByWeightDesc() {
        GameIndexEntity gameIndexEntity = gameIndexService.findByGameIndexUrlAndVersion("https://pokeapi.co/api/v2/version/1/", "red");
        return pokemonRepository.findTop5ByGameIndexEqualsOrderByWeightDesc(gameIndexEntity);
    }

    public List<PokemonEntity> findTop5ByGameIndexEqualsOrderByBaseExperienceDesc() {
        GameIndexEntity gameIndexEntity = gameIndexService.findByGameIndexUrlAndVersion("https://pokeapi.co/api/v2/version/1/", "red");
        return pokemonRepository.findTop5ByGameIndexEqualsOrderByBaseExperienceDesc(gameIndexEntity);
    }

    public List<PokemonEntity> convertPokemonsToEntities(List<Pokemon> pokemonList) {
        return pokemonList.stream()
                .map(x -> new PokemonEntityBuilder()
                        .withId(x.getId())
                        .withName(x.getName())
                        .withUrl(POKEMON_URL + x.getId())
                        .withHeight(x.getHeight())
                        .withWeight(x.getWeight())
                        .withLocationAreaEncounters(x.getLocationAreaEncounters())
                        .withBaseExperience(x.getBaseExperience())
                        .withIsDefault(x.getIsDefault())
                        .withOrder(x.getOrder())
                        .withGameIndex(gameIndexService.convertGameIndexesToEntity(x.getGameIndices()))
                        .build()
                )
                .collect(Collectors.toList());
    }
}
