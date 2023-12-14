package br.com.giunei.gestao_vagas.modules.company.dto;

import lombok.Data;

@Data
public class CreateJobDTO {

    private String description;
    private String benefits;
    private String level;
}
