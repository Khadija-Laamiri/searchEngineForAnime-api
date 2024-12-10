package org.projet.searchengineforanimeapi.services;

import org.projet.searchengineforanimeapi.dtos.AnimeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AnimeService{

    void importAnimeData(String filePath) throws Exception;

    List<AnimeDTO> search(String query) throws Exception;



}
