package com.example.pokemon.service;

import com.example.pokemon.dto.scheme.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class PokeApiClientTest {

    private PokeApiClient pokeApiClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void init() {
        pokeApiClient = new PokeApiClient(restTemplate);
    }

    @Test
    public void testGetPokemonSuccess() {
        // Given
        Mono<Pokemon> monoPokemon = pokeApiClient.getPokemon("api/v2/pokemon/1");
        // When
        Pokemon pokemon = monoPokemon.log().block();
        // Then
        assertThat(pokemon).isNotNull();
    }

    @Test
    public void testGetPokemonError() {
        // Given
        Mono<Pokemon> monoPokemon = pokeApiClient.getPokemon("api/v2/pokemon/59111");
        // When
        Pokemon pokemon = monoPokemon.log().block();
        // Then
        assertThat(pokemon).isNull();
    }

    @Test
    public void testFetchPokemons() {
        // Given
        List<String> s = List.of("api/v2/pokemon/1", "api/v2/pokemon/2");
        // When
        List<Pokemon> pokemon = pokeApiClient.fetchPokemons(s).collectList().block();
        // Then
        assertThat(pokemon.size()).isEqualTo(2);
    }

    @Test
    public void testFetchPokemons_WithError_404() {
        // Given
        List<String> s = List.of("api/v2/pokemon/1", "api/v2/pokemon/notFound");
        // When
        List<Pokemon> pokemon = pokeApiClient.fetchPokemons(s).collectList().block();
        // Then
        assertThat(pokemon.size()).isEqualTo(1);
    }
}