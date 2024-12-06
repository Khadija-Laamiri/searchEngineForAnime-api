package org.projet.searchengineforanimeapi.mappers;

import org.projet.searchengineforanimeapi.dtos.AnimeDTO;
import org.projet.searchengineforanimeapi.entities.Anime;

public class AnimeMapper {
    public static AnimeDTO toDto(Anime anime) {
        AnimeDTO dto = new AnimeDTO();
        dto.setId(anime.getId());
        dto.setTitle(anime.getTitle());
        dto.setScore(anime.getScore());
        dto.setShortDescription(generateShortDescription(anime.getDescription()));
        return dto;
    }

    private static String generateShortDescription(String description) {
        if (description == null || description.isEmpty()) {
            return "";
        }
        return description.length() > 50 ? description.substring(0, 50) + "..." : description;
    }
}
