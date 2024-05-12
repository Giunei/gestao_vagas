package br.com.giunei.gestao_vagas.modules.candidate.use_cases;

import br.com.giunei.gestao_vagas.exceptions.JobAlreadyAppliedException;
import br.com.giunei.gestao_vagas.exceptions.JobNotFoundException;
import br.com.giunei.gestao_vagas.exceptions.UserNotFoundException;
import br.com.giunei.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.giunei.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import br.com.giunei.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import br.com.giunei.gestao_vagas.modules.company.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApplyJobCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplyJobRepository applyJobRepository;

    public ApplyJobEntity execute(UUID idCandidate, UUID idJob, Integer rating) {
        this.candidateRepository.findById(idCandidate).orElseThrow(UserNotFoundException::new);

        this.jobRepository.findById(idJob).orElseThrow(JobNotFoundException::new);

        List<UUID> candidatesIds = this.applyJobRepository.findByJobId(idJob)
                .stream().map(ApplyJobEntity::getCandidateId).toList();

        if (candidatesIds.contains(idCandidate)) {
            throw new JobAlreadyAppliedException();
        }

        ApplyJobEntity applyJob = ApplyJobEntity.builder()
                .candidateId(idCandidate)
                .jobId(idJob)
                .rating(rating)
                .build();
        return applyJobRepository.save(applyJob);
    }
}
