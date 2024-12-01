package org.projet.searchengineforanimeapi.controllers;

import org.projet.searchengineforanimeapi.services.AnimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping("/import-anime")
    public String importAnime(@RequestParam String filePath) {
        try {
            animeService.importAnimeData(filePath);
            return "Anime data imported successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to import anime data: " + e.getMessage();
        }
    }
}
