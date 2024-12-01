package org.projet.searchengineforanimeapi.repositories;

import org.projet.searchengineforanimeapi.entities.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
}
