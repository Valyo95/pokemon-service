package com.example.pokemon.service;

import com.example.pokemon.dto.scheme.GameIndex;
import com.example.pokemon.entity.GameIndexEntity;
import com.example.pokemon.repository.GameIndexRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GameIndexService {
    private final GameIndexRepository gameIndexRepository;

    public GameIndexService(GameIndexRepository gameIndexRepository) {
        this.gameIndexRepository = gameIndexRepository;
    }

    @Transactional
    public List<GameIndexEntity> insertGameIndecies(List<GameIndexEntity> gameIndexEntities) {
        log.info("Started inserting game indexes");
        gameIndexEntities = gameIndexRepository.saveAll(gameIndexEntities);
        log.info("Game indexes inserted in DB");
        return gameIndexEntities;
    }

    public GameIndexEntity findByGameIndexUrlAndVersion(String url, String name) {
        log.info("Fetching Game Index with url:{} and name:{}", url, name);
        GameIndexEntity gameIndexEntity = gameIndexRepository.findGameIndexEntityByUrlAndName(url, name);
        log.info("Found: {}", gameIndexEntity);
        return gameIndexEntity;
    }

    public List<GameIndexEntity> convertGameIndexesToEntity(List<GameIndex> gameIndices) {
        return gameIndices
                .stream()
                .map(x -> new GameIndexEntity(x.getVersion().getUrl(), x.getVersion().getName(), x.getGameIndex()))
                .collect(Collectors.toList());
    }
}
