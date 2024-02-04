package br.com.giunei.gestao_vagas.modules.company.controller;

import br.com.giunei.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.giunei.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.giunei.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.giunei.gestao_vagas.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static br.com.giunei.gestao_vagas.utils.TestUtils.jsonToJSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CreateJobControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity()).build();
    }

    @Test
    void should_be_able_to_create_a_new_job() throws Exception {
        CompanyEntity company = CompanyEntity.builder()
                .description("COMPANY_DESCRIPTION")
                .email("email@company.com")
                .password("12345678")
                .username("Company_USERNAME")
                .name("COMPANY_NAME").build();

        company = companyRepository.saveAndFlush(company);

        CreateJobDTO createJobDTO = CreateJobDTO.builder()
                .benefits("BENEFITS_TESTS")
                .description("DESCRIPTION_TEST")
                .level("LEVEL_TEST").build();

        ResultActions result = mvc.perform(post("/company/job/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToJSON(createJobDTO))
                        .header("Authorization", TestUtils.generateToken(company.getId(), "GIUNASA_@123#")))
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(result);
    }

    @Test
    void should_not_be_able_to_creeate_to_create_a_new_job_if_company_not_found() throws Exception {
        CreateJobDTO createJobDTO = CreateJobDTO.builder()
                .benefits("BENEFITS_TESTS")
                .description("DESCRIPTION_TEST")
                .level("LEVEL_TEST").build();

        mvc.perform(post("/company/job/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToJSON(createJobDTO))
                        .header("Authorization", TestUtils.generateToken(UUID.randomUUID(), "GIUNASA_@123#")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}