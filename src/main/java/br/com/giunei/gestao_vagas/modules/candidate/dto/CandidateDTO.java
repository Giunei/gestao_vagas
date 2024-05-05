package br.com.giunei.gestao_vagas.modules.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDTO {

    private UUID id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String description;
    private String linkedInURL;
}
