package br.com.giunei.gestao_vagas.modules.candidate.use_cases;

import br.com.giunei.gestao_vagas.exceptions.UserNotFoundException;
import br.com.giunei.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.giunei.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.giunei.gestao_vagas.modules.candidate.entity.CandidateEntity;
import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import br.com.giunei.gestao_vagas.modules.company.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileCandidateUseCaseTest {

    @InjectMocks
    private ProfileCandidateUseCase profileCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobRepository jobRepository;

    @Test
    @DisplayName("Should return candidate profile with applied jobs")
    void should_return_candidate_profile_with_applied_jobs() {
        UUID idCandidate = UUID.randomUUID();

        CandidateEntity candidate = CandidateEntity.builder()
                .id(idCandidate)
                .description("Java Developer")
                .username("johndoe")
                .email("johndoe@example.com")
                .name("John Doe")
                .build();

        JobEntity job1 = JobEntity.builder()
                .id(UUID.randomUUID())
                .description("Java Developer")
                .build();

        JobEntity job2 = JobEntity.builder()
                .id(UUID.randomUUID())
                .description("Senior Java Developer")
                .build();

        List<JobEntity> jobs = List.of(job1, job2);

        when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(candidate));
        when(jobRepository.findByCandidateId(idCandidate)).thenReturn(jobs);

        ProfileCandidateResponseDTO result = profileCandidateUseCase.execute(idCandidate);

        assertThat(result.getId()).isEqualTo(candidate.getId());
        assertThat(result.getDescription()).isEqualTo(candidate.getDescription());
        assertThat(result.getUsername()).isEqualTo(candidate.getUsername());
        assertThat(result.getEmail()).isEqualTo(candidate.getEmail());
        assertThat(result.getName()).isEqualTo(candidate.getName());
        assertThat(result.getVagasAplicadas()).containsExactly("Java Developer", "Senior Java Developer");
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when candidate does not exist")
    void should_throw_user_not_found_exception_when_candidate_does_not_exist() {
        UUID idCandidate = UUID.randomUUID();

        when(candidateRepository.findById(idCandidate)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> profileCandidateUseCase.execute(idCandidate));
    }

    @Test
    @DisplayName("Should return empty list of applied jobs when candidate has none")
    void should_return_empty_list_of_applied_jobs_when_candidate_has_none() {
        UUID idCandidate = UUID.randomUUID();

        CandidateEntity candidate = CandidateEntity.builder()
                .id(idCandidate)
                .description("Java Developer")
                .username("johndoe")
                .email("johndoe@example.com")
                .name("John Doe")
                .build();

        when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(candidate));
        when(jobRepository.findByCandidateId(idCandidate)).thenReturn(Collections.emptyList());

        ProfileCandidateResponseDTO result = profileCandidateUseCase.execute(idCandidate);

        assertThat(result.getVagasAplicadas()).isEmpty();
    }
}