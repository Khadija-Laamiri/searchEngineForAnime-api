package org.projet.searchengineforanimeapi.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.projet.searchengineforanimeapi.dtos.AnimeDTO;
import org.projet.searchengineforanimeapi.dtos.Result;
import org.projet.searchengineforanimeapi.dtos.SearchResult;
import org.projet.searchengineforanimeapi.entities.Anime;
import org.projet.searchengineforanimeapi.mappers.AnimeMapper;
import org.projet.searchengineforanimeapi.repositories.AnimeRepository;
import org.projet.searchengineforanimeapi.services.AnimeService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AnimeServiceImlp implements AnimeService {
    private final AnimeRepository animeRepository; // Your JPA repository for Anime
    private final RestTemplate restTemplate;

    public AnimeServiceImlp(AnimeRepository animeRepository, RestTemplate restTemplate) {
        this.animeRepository = animeRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public void importAnimeData(String filePath) throws Exception {
        // Parse JSON file
        ObjectMapper mapper = new ObjectMapper();
        List<Anime> animes = mapper.readValue(new File(filePath), new TypeReference<List<Anime>>() {});

        // Save data to the database
        animeRepository.saveAll(animes);
    }

    @Override
    public List<AnimeDTO> search(String query){
        String url = "http://127.0.0.1:8000/search?query=" + query;
        List <AnimeDTO> animeList = new ArrayList<>();
        // Perform the HTTP call
        SearchResult docs = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                SearchResult.class
        ).getBody();

        if (docs != null && docs.getResults() != null) {
            List<Result> documents = docs.getResults();
            for (Result result : documents) {

                Anime anime = animeRepository.findAnimeBy_Doc_name(result.getDoc());

                animeList.add(AnimeMapper.toDto(anime));
            }

        }

        return animeList;

    }



}
