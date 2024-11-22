package org.projet.searchengineforanimeapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInput {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
