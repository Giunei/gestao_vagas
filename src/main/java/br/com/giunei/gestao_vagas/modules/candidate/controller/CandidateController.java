package br.com.giunei.gestao_vagas.modules.candidate.controller;

import br.com.giunei.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.giunei.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import br.com.giunei.gestao_vagas.modules.candidate.entity.CandidateEntity;
import br.com.giunei.gestao_vagas.modules.candidate.use_cases.*;
import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidato", description = "Informações do candidato")
public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private ProfileCandidateUseCase profileCandidateUseCase;

    @Autowired
    private ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

    @Autowired
    private ApplyJobCandidateUseCase applyJobCandidateUseCase;

    @Autowired
    private ListAllJobsByFilterOrderByRating listAllJobsByFilterOrderByRating;

    @Autowired
    private ListAllJobsByFilterOrderByDataUseCase listAllJobsByFilterOrderByDataUseCase;

    @PostMapping("/")
    @Operation(summary = "Cadastro de candidato", description = "Essa função é responsável por cadastrar um candidato")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = CandidateEntity.class))
            }),
            @ApiResponse(responseCode = "400", description = "Usuário já existe")
    })
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
    @Operation(summary = "Perfil do candidato", description = "Essa função é responsável por buscar as informações do candidato")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProfileCandidateResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    @SecurityRequirement(name = "jwt_auth")
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

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Listagem de vagas disponível para o candidato", description = "Essa função é responsável por listar todas as vagas disponiveis baseada nos filtros." +
            "Passar specification como 'data' para ordenar por mais recentes, 'rating' por mais bem votadas, caso contrário apenas busca pelo filtro.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = JobEntity.class))
                    )
            })
    })
    @SecurityRequirement(name = "jwt_auth")
    public List<JobEntity> findJobByFilter(@RequestParam String filter, @RequestParam String specification) {
        if(Objects.equals(specification, "data")) {
            return this.listAllJobsByFilterOrderByDataUseCase.execute(filter);
        } else if (Objects.equals(specification, "rating")) {
            return this.listAllJobsByFilterOrderByRating.execute(filter);
        }
        return this.listAllJobsByFilterUseCase.execute(filter);
    }

    @PostMapping("/jobs/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @SecurityRequirement(name = "jwt_auth")
    @Operation(summary = "Incrição do candidato para uma vaga", description = "Essa função é responsável por realizar a inscrição em um vaga.")
    public ResponseEntity<Object> applyJob(HttpServletRequest request, @RequestParam UUID jobId, @RequestParam Integer rating) {
        var idCandidate = request.getAttribute("candidate_id");

        try {
            ApplyJobEntity result = this.applyJobCandidateUseCase.execute(UUID.fromString(idCandidate.toString()), jobId, rating);
            return ResponseEntity.ok().body(result);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
