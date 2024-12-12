package org.projet.searchengineforanimeapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponse {

    private  String correct_query;
    private List<AnimeDTO> results;


}
