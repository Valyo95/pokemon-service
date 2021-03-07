package com.example.pokemon.service;

import com.example.pokemon.dto.PokemonList;
import com.example.pokemon.dto.scheme.Pokemon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PokeApiClient {

    private final String POKE_API_BASE_URL = "https://pokeapi.co";

    private final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build();

    private final WebClient webClient = WebClient.builder()
            .baseUrl(POKE_API_BASE_URL)
            .exchangeStrategies(exchangeStrategies)
            .build();

    @Value("${pokemon.clearPokemonApiCache}")
    private long clearPokemonCacheInterval;

    private final RestTemplate restTemplate;

    public PokeApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    private void initialize() {
        log.info("Going to clear pokemon cache every {} ms", clearPokemonCacheInterval);
    }

    @Cacheable("pokemons")
    public List<Pokemon> getAllPokemons() {
        int size = getPokemonsSize();
        log.info("Trying to fetch {} pokemons", size);

        PokemonList pokemonList = restTemplate.getForObject(POKE_API_BASE_URL
                + "/api/v2/pokemon?offset=0&limit=" + size, PokemonList.class);

        List<String> pokemonUrls = pokemonList
                .getResults()
                .stream()
                .map(x -> {
                    try {
                        return new URL(x.getUrl()).getPath();
                    } catch (MalformedURLException e) {
                        log.error("Pokemon: '{}' url is not valid: '{}'", x.getName(), x.getUrl(), e);
                        return null;
                    }
                })
                .filter(x -> x != null)
                .collect(Collectors.toList());

        Flux<Pokemon> pokeFlux = fetchPokemons(pokemonUrls);
        List<Pokemon> pokemons = pokeFlux.collectList().block();
        log.info("Finished gathering {} pokemons", pokemons.size());
        return pokemons;
    }

    public Mono<Pokemon> getPokemon(String path) {
        return webClient.get()
                .uri(path)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.error("Error when trying to retrieve pokemon from {}, HTTP Status Code: {}",
                            POKE_API_BASE_URL + path, clientResponse.statusCode());
                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Pokemon not found."));
                    } else {
                        return Mono.error(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error."));
                    }
                })
                .bodyToMono(Pokemon.class)
                .onErrorResume(x -> Mono.empty());
        //.log();
    }

    public Flux<Pokemon> fetchPokemons(List<String> pokemonUrls) {
        return Flux.fromIterable(pokemonUrls)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(this::getPokemon)
                //.sequential();
                .ordered((u1, u2) -> u2.getId() - u1.getId());
    }

    private int getPokemonsSize() {
        PokemonList pokemonIterating = restTemplate.getForObject(POKE_API_BASE_URL + "/api/v2/pokemon", PokemonList.class);
        return pokemonIterating.getCount();
    }

    @Scheduled(fixedRateString ="${pokemon.clearPokemonApiCache}")
    @CacheEvict(value = {"pokemons"})
    public void clearCache() {
        log.info("Clearing pokemons cache");
    }


//    public List<PokemonEntity> getAll() throws MalformedURLException {
//        int size = getPokemonsSize();
//        log.info("Pokemons count: {}", size);
//
//        PokemonList pokemonList = restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon?offset=0&limit=" + size, PokemonList.class);
//        log.info("Pokemons list: {}", pokemonList);
//
//        List<Pokemon> pokemons = new ArrayList<>();
//        for (PokemonEntry pokemomResult: pokemonList.getResults()) {
//            String path = new URL(pokemomResult.getUrl()).getPath();
//            log.info("path: {}", path);
//            Pokemon pokemon = restTemplate.getForObject(pokemomResult.getUrl(), Pokemon.class);
//            pokemons.add(pokemon);
//        }
//        log.info("Finished gathering the pokemons");
//        return insertPokemons(pokemons);
//    }
}
