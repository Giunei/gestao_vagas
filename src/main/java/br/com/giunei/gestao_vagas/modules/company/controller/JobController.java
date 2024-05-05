package br.com.giunei.gestao_vagas.modules.company.controller;

import br.com.giunei.gestao_vagas.modules.candidate.dto.ApplyJobDTO;
import br.com.giunei.gestao_vagas.modules.candidate.use_cases.ListAllApplyJobsByJobIdUseCase;
import br.com.giunei.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.giunei.gestao_vagas.modules.company.entities.JobEntity;
import br.com.giunei.gestao_vagas.modules.company.use_cases.CreateJobUseCase;
import br.com.giunei.gestao_vagas.modules.company.use_cases.ListAllJobsByCompanyUseCase;
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
import java.util.UUID;

@RestController
@RequestMapping("/company/job")
public class JobController {

    @Autowired
    private CreateJobUseCase createJobUseCase;

    @Autowired
    private ListAllJobsByCompanyUseCase listAllJobsByCompanyUseCase;

    @Autowired
    private ListAllApplyJobsByJobIdUseCase allApplyJobsByJobIdUseCase;

    @PostMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    @Tag(name = "Vagas", description = "Informações das vagas")
    @Operation(summary = "Cadastro de vagas", description = "Essa função é responsável por cadastrar as vagas dentro da empresa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = JobEntity.class))
            })
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateJobDTO createJobDTO, HttpServletRequest request) {
        Object companyId = request.getAttribute("company_id");

        try {
            JobEntity jobEntity = JobEntity.builder()
                    .benefits(createJobDTO.getBenefits())
                    .companyId(UUID.fromString(companyId.toString()))
                    .description(createJobDTO.getDescription())
                    .level(createJobDTO.getLevel()).build();

            JobEntity result = this.createJobUseCase.execute(jobEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/")
    @PreAuthorize("hasRole('COMPANY')")
    @Tag(name = "Vagas", description = "Listagem das vagas")
    @Operation(summary = "Listagem de vagas", description = "Essa função é responsável por listar as vagas da empresa")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = JobEntity.class))
    })
    public ResponseEntity<Object> listCompany(HttpServletRequest request) {
        Object companyId = request.getAttribute("company_id");
        List<JobEntity> result = listAllJobsByCompanyUseCase.execute(UUID.fromString(companyId.toString()));
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/applieds")
    @PreAuthorize("hasRole('COMPANY')")
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
    public List<ApplyJobDTO> findApplyJobByJobId(@RequestParam UUID jobId) {
        return this.allApplyJobsByJobIdUseCase.execute(jobId);
    }
}
