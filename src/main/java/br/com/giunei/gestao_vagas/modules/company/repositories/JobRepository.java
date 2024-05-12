package br.com.giunei.gestao_vagas.modules.company.repositories;

import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {

    List<JobEntity> findByDescriptionContainingIgnoreCase(String filter);
    List<JobEntity> findByDescriptionContainingIgnoreCaseOrderByCreatedDate(String filter);
    List<JobEntity> findByCompanyId(UUID companyId);

    @Query("""
            SELECT j
                FROM job j
            WHERE j.id = :jobId
                AND lower(j.description) LIKE lower(concat('%', :filter, '%'))
            """)
    List<JobEntity> findByJobEntityAndDescription(UUID jobId, String filter);

    @Query("""
            SELECT ji
                FROM apply_jobs aj
            JOIN aj.jobEntity ji
            WHERE aj.candidateId =:candidateId
            """)
    List<JobEntity> findByCandidateId(UUID candidateId);
}
