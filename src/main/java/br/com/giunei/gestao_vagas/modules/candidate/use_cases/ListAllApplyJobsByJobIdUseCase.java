package br.com.giunei.gestao_vagas.modules.candidate.use_cases;

import br.com.giunei.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.giunei.gestao_vagas.modules.candidate.dto.ApplyJobDTO;
import br.com.giunei.gestao_vagas.modules.candidate.dto.CandidateDTO;
import br.com.giunei.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import br.com.giunei.gestao_vagas.modules.candidate.entity.CandidateEntity;
import br.com.giunei.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListAllApplyJobsByJobIdUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ApplyJobRepository applyJobRepository;

    public List<ApplyJobDTO> execute(UUID jobId) {
        return applyJobRepository.findByJobId(jobId).stream()
                .map(this::applyJobEntityToDTO)
                .toList();
    }

    private ApplyJobDTO applyJobEntityToDTO(ApplyJobEntity applyJobEntity) {
        return ApplyJobDTO.builder()
                .id(applyJobEntity.getId())
                .candidate(candidateEntityToDTO(applyJobEntity.getCandidateEntity()))
                .candidateId(applyJobEntity.getId())
                .jobId(applyJobEntity.getJobId())
                .build();
    }

    private CandidateDTO candidateEntityToDTO(CandidateEntity candidateEntity) {
        return CandidateDTO.builder()
                .id(candidateEntity.getId())
                .email(candidateEntity.getEmail())
                .name(candidateEntity.getName())
                .linkedInURL(candidateEntity.getLinkedInURL())
                .build();
    }
}
