package br.com.giunei.gestao_vagas.modules.company.repositories;

import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {

    List<JobEntity> findByDescriptionContainingIgnoreCase(String filter);
    List<JobEntity> findByCompanyId(UUID companyId);
}
