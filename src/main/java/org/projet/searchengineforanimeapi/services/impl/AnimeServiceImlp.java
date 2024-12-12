package org.projet.searchengineforanimeapi.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.projet.searchengineforanimeapi.dtos.AnimeDTO;
import org.projet.searchengineforanimeapi.dtos.Result;
import org.projet.searchengineforanimeapi.dtos.SearchResponse;
import org.projet.searchengineforanimeapi.dtos.SearchResult;
import org.projet.searchengineforanimeapi.entities.Anime;
import org.projet.searchengineforanimeapi.mappers.AnimeMapper;
import org.projet.searchengineforanimeapi.repositories.AnimeRepository;
import org.projet.searchengineforanimeapi.repositories.UserRepo;
import org.projet.searchengineforanimeapi.services.AnimeService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.*;

@Service
public class AnimeServiceImlp implements AnimeService {
    private final AnimeRepository animeRepository; // Your JPA repository for Anime
    private final RestTemplate restTemplate;
    private final UserRepo userRepo;

    public AnimeServiceImlp(AnimeRepository animeRepository, RestTemplate restTemplate, UserRepo userRepo) {
        this.animeRepository = animeRepository;
        this.restTemplate = restTemplate;
        this.userRepo = userRepo;
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
    public SearchResponse search(String query, Long id){

        String url = "http://127.0.0.1:8000/search?query=" + query;
        Set<Long> saved_animes = Set.of();
        List <AnimeDTO> animeList = new ArrayList<>();
        // Perform the HTTP call
        SearchResult docs = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                SearchResult.class
        ).getBody();
        if(id != null){
             saved_animes=userRepo.findAnimesByUserId(id);
        }

        if (docs != null && docs.getResults() != null) {
            List<Result> documents = docs.getResults();
            for (Result result : documents) {

                Anime anime = animeRepository.findAnimeBy_Doc_name(result.getDoc());
                AnimeDTO animeDTO = AnimeMapper.toDto(anime);
                if(id != null && !saved_animes.isEmpty()){
                    animeDTO.setSaved(saved_animes.contains(animeDTO.getId()));
                }
                animeList.add(animeDTO);
            }

        }
        assert docs != null;
        return new SearchResponse(docs.getCorrect_query(),animeList);

    }



}
