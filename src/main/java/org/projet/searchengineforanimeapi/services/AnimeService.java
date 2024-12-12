package org.projet.searchengineforanimeapi.services;

import org.projet.searchengineforanimeapi.dtos.AnimeDTO;
import org.projet.searchengineforanimeapi.dtos.SearchResponse;

import java.util.List;

public interface AnimeService{

    void importAnimeData(String filePath) throws Exception;

    SearchResponse search(String query, Long id) throws Exception;



}
