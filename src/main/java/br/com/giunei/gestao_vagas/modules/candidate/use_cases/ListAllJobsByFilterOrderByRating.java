package br.com.giunei.gestao_vagas.modules.candidate.use_cases;

import br.com.giunei.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import br.com.giunei.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import br.com.giunei.gestao_vagas.modules.company.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListAllJobsByFilterOrderByRating {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplyJobRepository applyJobRepository;

    public List<JobEntity> execute(String filter) {
        HashSet<UUID> jobsIds = applyJobRepository.findApplyJobCompanyIdOrderByRating()
                .stream().map(ApplyJobEntity::getJobId).collect(Collectors.toCollection(HashSet::new));
        return jobRepository.findByJobEntityListInAndDescriptionEquals(jobsIds, filter);
    }
}
