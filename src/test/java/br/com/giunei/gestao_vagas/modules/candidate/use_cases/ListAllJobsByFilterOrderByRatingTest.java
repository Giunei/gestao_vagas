package br.com.giunei.gestao_vagas.modules.candidate.use_cases;

import br.com.giunei.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllJobsByFilterOrderByRatingTest {

    @InjectMocks
    private ListAllJobsByFilterOrderByRating listAllJobsByFilterOrderByRating;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private ApplyJobRepository applyJobRepository;

    @Test
    @DisplayName("Should list all jobs by filter and order by rating")
    void should_list_all_jobs_by_filter_and_order_by_rating() {
        String filter = "java";

        UUID jobId1 = UUID.randomUUID();
        UUID jobId2 = UUID.randomUUID();

        List<UUID> bestJobs = List.of(jobId1, jobId2);

        JobEntity job1 = JobEntity.builder()
                .id(jobId1)
                .description("Java Developer")
                .build();

        JobEntity job2 = JobEntity.builder()
                .id(jobId2)
                .description("Senior Java Developer")
                .build();

        when(applyJobRepository.findBestJobId()).thenReturn(bestJobs);
        when(jobRepository.findByJobEntityAndDescription(jobId1, filter))
                .thenReturn(List.of(job1));
        when(jobRepository.findByJobEntityAndDescription(jobId2, filter))
                .thenReturn(List.of(job2));

        List<JobEntity> result = listAllJobsByFilterOrderByRating.execute(filter);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDescription()).isEqualTo("Java Developer");
        assertThat(result.get(1).getDescription()).isEqualTo("Senior Java Developer");
    }

    @Test
    @DisplayName("Should return empty list when no jobs match the filter")
    void should_return_empty_list_when_no_jobs_match_the_filter() {
        String filter = "python";

        List<UUID> bestJobs = Collections.emptyList();

        when(applyJobRepository.findBestJobId()).thenReturn(bestJobs);

        List<JobEntity> result = listAllJobsByFilterOrderByRating.execute(filter);

        assertThat(result).isEmpty();
    }
}