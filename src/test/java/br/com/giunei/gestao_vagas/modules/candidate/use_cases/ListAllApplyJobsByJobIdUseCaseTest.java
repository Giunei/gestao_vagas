package br.com.giunei.gestao_vagas.modules.candidate.use_cases;

import br.com.giunei.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.giunei.gestao_vagas.modules.candidate.dto.ApplyJobDTO;
import br.com.giunei.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import br.com.giunei.gestao_vagas.modules.candidate.entity.CandidateEntity;
import br.com.giunei.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllApplyJobsByJobIdUseCaseTest {

    @InjectMocks
    private ListAllApplyJobsByJobIdUseCase listAllApplyJobsByJobIdUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private ApplyJobRepository applyJobRepository;

    @Test
    @DisplayName("Should list all apply jobs by job ID")
    void should_list_all_apply_jobs_by_job_id() {
        UUID jobId = UUID.randomUUID();

        CandidateEntity candidate1 = CandidateEntity.builder()
                .id(UUID.randomUUID())
                .email("candidate1@example.com")
                .name("Candidate 1")
                .linkedInURL("https://www.linkedin.com/in/candidate1/")
                .build();

        CandidateEntity candidate2 = CandidateEntity.builder()
                .id(UUID.randomUUID())
                .email("candidate2@example.com")
                .name("Candidate 2")
                .linkedInURL("https://www.linkedin.com/in/candidate2/")
                .build();

        ApplyJobEntity applyJob1 = ApplyJobEntity.builder()
                .id(UUID.randomUUID())
                .candidateId(candidate1.getId())
                .jobId(jobId)
                .candidateEntity(candidate1)
                .build();

        ApplyJobEntity applyJob2 = ApplyJobEntity.builder()
                .id(UUID.randomUUID())
                .candidateId(candidate2.getId())
                .jobId(jobId)
                .candidateEntity(candidate2)
                .build();

        when(applyJobRepository.findByJobId(jobId))
                .thenReturn(List.of(applyJob1, applyJob2));

        List<ApplyJobDTO> result = listAllApplyJobsByJobIdUseCase.execute(jobId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCandidate().getName()).isEqualTo("Candidate 1");
        assertThat(result.get(1).getCandidate().getName()).isEqualTo("Candidate 2");
    }

    @Test
    @DisplayName("Should return empty list when job ID does not exist or has no applicants")
    void should_return_empty_list_when_job_id_does_not_exist_or_has_no_applicants() {
        UUID jobId = UUID.randomUUID();

        when(applyJobRepository.findByJobId(jobId)).thenReturn(Collections.emptyList());

        List<ApplyJobDTO> result = listAllApplyJobsByJobIdUseCase.execute(jobId);

        assertThat(result).isEmpty();
    }
}
