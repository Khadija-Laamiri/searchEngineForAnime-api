package org.projet.searchengineforanimeapi.repositories;

import org.projet.searchengineforanimeapi.entities.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

     @Query(value = "SELECT a FROM Anime a WHERE a.doc_name = :docName")
     Anime findAnimeBy_Doc_name(String docName);

}
