package br.com.giunei.gestao_vagas.modules.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyJobDTO {

    private UUID id;
    private CandidateDTO candidate;
    private UUID candidateId;
    private UUID jobId;
}
