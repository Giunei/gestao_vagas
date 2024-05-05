package br.com.giunei.gestao_vagas.modules.candidate.use_cases;

import br.com.giunei.gestao_vagas.exceptions.UserFoundException;
import br.com.giunei.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.giunei.gestao_vagas.modules.candidate.dto.CandidateDTO;
import br.com.giunei.gestao_vagas.modules.candidate.entity.CandidateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateEntity execute(CandidateDTO candidateDTO) {
        this.candidateRepository
                .findByUsernameOrEmail(candidateDTO.getUsername(), candidateDTO.getEmail())
                .ifPresent(user -> {
                    throw new UserFoundException();
                });

        String password = passwordEncoder.encode(candidateDTO.getPassword());
        candidateDTO.setPassword(password);

        CandidateEntity candidateEntity = candidateDTOToEntity(candidateDTO);

        return this.candidateRepository.save(candidateEntity);
    }

    private CandidateEntity candidateDTOToEntity(CandidateDTO candidateDTO) {
        return CandidateEntity.builder()
                .id(candidateDTO.getId())
                .username(candidateDTO.getUsername())
                .description(candidateDTO.getDescription())
                .password(candidateDTO.getPassword())
                .email(candidateDTO.getEmail())
                .name(candidateDTO.getName())
                .linkedInURL(candidateDTO.getLinkedInURL())
                .build();
    }
}
