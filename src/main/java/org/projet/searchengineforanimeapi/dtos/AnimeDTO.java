package org.projet.searchengineforanimeapi.dtos;

import lombok.Data;

@Data
public class AnimeDTO {
    private Long id;
    private String title;
    private Double score;
    private String shortDescription;
    private String doc_name;
    private Boolean saved;
}
