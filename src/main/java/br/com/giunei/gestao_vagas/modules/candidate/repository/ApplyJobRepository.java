package br.com.giunei.gestao_vagas.modules.candidate.repository;

import br.com.giunei.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ApplyJobRepository extends JpaRepository<ApplyJobEntity, UUID> {

    @Query("""
            SELECT ap.jobId
                FROM apply_jobs ap
            GROUP BY ap.jobId
            ORDER BY AVG(ap.rating) DESC
            """)
    List<UUID> findBestJobId();
}
