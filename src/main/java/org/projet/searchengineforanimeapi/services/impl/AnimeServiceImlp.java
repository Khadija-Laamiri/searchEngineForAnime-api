package org.projet.searchengineforanimeapi.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.projet.searchengineforanimeapi.entities.Anime;
import org.projet.searchengineforanimeapi.repositories.AnimeRepository;
import org.projet.searchengineforanimeapi.services.AnimeService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class AnimeServiceImlp implements AnimeService {
    private final AnimeRepository animeRepository; // Your JPA repository for Anime

    public AnimeServiceImlp(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    @Override
    public void importAnimeData(String filePath) throws Exception {
        // Parse JSON file
        ObjectMapper mapper = new ObjectMapper();
        List<Anime> animes = mapper.readValue(new File(filePath), new TypeReference<List<Anime>>() {});

        // Save data to the database
        animeRepository.saveAll(animes);
    }
}
