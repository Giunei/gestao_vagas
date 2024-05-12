package br.com.giunei.gestao_vagas.modules.company.use_cases;

import br.com.giunei.gestao_vagas.exceptions.CompanyNotFoundException;
import br.com.giunei.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import br.com.giunei.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.giunei.gestao_vagas.modules.company.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateJobUseCaseTest {

    @InjectMocks
    private CreateJobUseCase createJobUseCase;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("Should create job successfully")
    void should_create_job_successfully() {
        UUID companyId = UUID.randomUUID();
        JobEntity jobEntity = JobEntity.builder()
                .id(UUID.randomUUID())
                .description("Software Engineer")
                .companyId(companyId)
                .build();

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(new CompanyEntity()));
        when(jobRepository.save(jobEntity)).thenReturn(jobEntity);

        JobEntity result = createJobUseCase.execute(jobEntity);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(jobEntity.getDescription());
        assertThat(result.getCompanyId()).isEqualTo(jobEntity.getCompanyId());
    }

    @Test
    @DisplayName("Should throw CompanyNotFoundException when company is not found")
    void should_throw_company_not_found_exception_when_company_is_not_found() {
        JobEntity jobEntity = JobEntity.builder()
                .id(UUID.randomUUID())
                .description("Software Engineer")
                .companyId(UUID.randomUUID()) // Empresa inexistente
                .build();

        when(companyRepository.findById(jobEntity.getCompanyId())).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> createJobUseCase.execute(jobEntity));
    }
}