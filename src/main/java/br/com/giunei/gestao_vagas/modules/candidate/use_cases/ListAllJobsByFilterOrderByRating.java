package br.com.giunei.gestao_vagas.modules.candidate.use_cases;

import br.com.giunei.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import br.com.giunei.gestao_vagas.modules.company.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ListAllJobsByFilterOrderByRating {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplyJobRepository applyJobRepository;

    public List<JobEntity> execute(String filter) {
        List<UUID> bestJobs = applyJobRepository.findBestJobId();
        List<JobEntity> jobEntities = new ArrayList<>();

        for(UUID id : bestJobs) {
            jobEntities.add(jobRepository.findByJobEntityInAndDescriptionEquals(id, filter));
        }
        return jobEntities;
    }
}
