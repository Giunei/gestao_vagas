package br.com.giunei.gestao_vagas.modules.candidate.repository;

import br.com.giunei.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ApplyJobRepository extends JpaRepository<ApplyJobEntity, UUID> {

    @Query("""
            SELECT aj
                FROM apply_jobs aj
            JOIN aj.jobEntity j
            ORDER BY aj.rating DESC
            """)
    List<ApplyJobEntity> findApplyJobCompanyIdOrderByRating();
}
