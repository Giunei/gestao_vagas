package br.com.giunei.gestao_vagas.modules.company.repositories;

import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {

    List<JobEntity> findByDescriptionContainingIgnoreCase(String filter);
    List<JobEntity> findByDescriptionContainingIgnoreCaseOrderByCreatedDate(String filter);
    List<JobEntity> findByCompanyId(UUID companyId);

    @Query("""
            SELECT j
                FROM job j
            WHERE j.id in :jobsId
                AND lower(j.description) LIKE lower(concat('%', :filter, '%'))
            """)
    List<JobEntity> findByJobEntityListInAndDescriptionEquals(HashSet<UUID> jobsId, String filter);
}
