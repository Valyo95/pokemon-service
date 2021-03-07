package com.example.pokemon.repository;

import com.example.pokemon.entity.GameIndexEntity;
import com.example.pokemon.entity.VersionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameIndexRepository extends JpaRepository<GameIndexEntity, VersionId> {
    GameIndexEntity findGameIndexEntityByUrlAndName(String url, String name);
}
