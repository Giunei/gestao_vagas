package br.com.giunei.gestao_vagas.modules.candidate.use_cases;

import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import br.com.giunei.gestao_vagas.modules.company.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllJobsByFilterOrderByDataUseCaseTest {

    @InjectMocks
    private ListAllJobsByFilterOrderByDataUseCase listAllJobsByFilterOrderByDataUseCase;

    @Mock
    private JobRepository jobRepository;

    @Test
    @DisplayName("Should list all jobs by filter and order by data")
    void should_list_all_jobs_by_filter_and_order_by_data() {
        String filter = "java";

        JobEntity job1 = JobEntity.builder()
                .id(UUID.randomUUID())
                .description("Java Developer")
                .createdDate(LocalDateTime.now())
                .build();

        JobEntity job2 = JobEntity.builder()
                .id(UUID.randomUUID())
                .description("Senior Java Developer")
                .createdDate(LocalDateTime.now().minusDays(1))
                .build();

        when(jobRepository.findByDescriptionContainingIgnoreCaseOrderByCreatedDate(filter))
                .thenReturn(List.of(job1, job2));

        List<JobEntity> result = listAllJobsByFilterOrderByDataUseCase.execute(filter);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDescription()).isEqualTo("Java Developer");
        assertThat(result.get(1).getDescription()).isEqualTo("Senior Java Developer");
    }

    @Test
    @DisplayName("Should return empty list when no jobs match the filter")
    void should_return_empty_list_when_no_jobs_match_the_filter() {
        String filter = "python";

        when(jobRepository.findByDescriptionContainingIgnoreCaseOrderByCreatedDate(filter))
                .thenReturn(Collections.emptyList());

        List<JobEntity> result = listAllJobsByFilterOrderByDataUseCase.execute(filter);

        assertThat(result).isEmpty();
    }
}