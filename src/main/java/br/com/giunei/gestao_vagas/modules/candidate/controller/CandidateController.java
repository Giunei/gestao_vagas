package br.com.giunei.gestao_vagas.modules.candidate.controller;

import br.com.giunei.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.giunei.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.giunei.gestao_vagas.modules.candidate.use_cases.CreateCandidateUseCase;
import br.com.giunei.gestao_vagas.modules.candidate.use_cases.ProfileCandidateUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private ProfileCandidateUseCase profileCandidateUseCase;

    @PostMapping("/")
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity) {
        try {
            var result = createCandidateUseCase.execute(candidateEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Object> get(HttpServletRequest request) {
        Object candidateId = request.getAttribute("candidate_id");
        try {
            ProfileCandidateResponseDTO profile = profileCandidateUseCase
                    .execute(UUID.fromString(candidateId.toString()));
            return ResponseEntity.ok().body(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
