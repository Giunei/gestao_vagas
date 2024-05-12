package br.com.giunei.gestao_vagas.modules.company.use_cases;

import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import br.com.giunei.gestao_vagas.modules.company.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ListAllJobsByCompanyUseCaseTest {

    @InjectMocks
    private ListAllJobsByCompanyUseCase listAllJobsByCompanyUseCase;

    @Mock
    private JobRepository jobRepository;

    @Test
    @DisplayName("Should return list of jobs for a company")
    void should_return_list_of_jobs_for_a_company() {
        UUID companyId = UUID.randomUUID();
        List<JobEntity> jobs = Arrays.asList(
                JobEntity.builder().id(UUID.randomUUID()).description("Job 1").companyId(companyId).build(),
                JobEntity.builder().id(UUID.randomUUID()).description("Job 2").companyId(companyId).build()
        );

        when(jobRepository.findByCompanyId(companyId)).thenReturn(jobs);

        List<JobEntity> result = listAllJobsByCompanyUseCase.execute(companyId);

        assertThat(result).isNotNull();
        assertThat(result.get(0).getDescription()).isEqualTo("Job 1");
        assertThat(result.get(1).getDescription()).isEqualTo("Job 2");
    }
}