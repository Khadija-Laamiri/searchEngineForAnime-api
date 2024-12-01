package org.projet.searchengineforanimeapi.services;

import org.springframework.stereotype.Service;

public interface AnimeService{

    void importAnimeData(String filePath) throws Exception;
}
