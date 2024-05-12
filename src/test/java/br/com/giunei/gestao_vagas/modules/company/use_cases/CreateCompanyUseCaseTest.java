package br.com.giunei.gestao_vagas.modules.company.use_cases;

import br.com.giunei.gestao_vagas.exceptions.UserFoundException;
import br.com.giunei.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.giunei.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCompanyUseCaseTest {

    @InjectMocks
    private CreateCompanyUseCase createCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should create company successfully")
    void should_create_company_successfully() {
        CompanyEntity company = CompanyEntity.builder()
                .id(UUID.randomUUID())
                .username("company")
                .email("company@example.com")
                .password("password")
                .build();

        when(companyRepository.findByUsernameOrEmail(company.getUsername(), company.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(company.getPassword())).thenReturn("encodedPassword");
        when(companyRepository.save(company)).thenReturn(company);

        CompanyEntity result = createCompanyUseCase.execute(company);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(company.getUsername());
        assertThat(result.getEmail()).isEqualTo(company.getEmail());
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    @DisplayName("Should throw UserFoundException when username is already in use")
    void should_throw_user_found_exception_when_username_is_already_in_use() {
        CompanyEntity company = CompanyEntity.builder()
                .id(UUID.randomUUID())
                .username("company")
                .email("company@example.com")
                .password("password")
                .build();

        when(companyRepository.findByUsernameOrEmail(company.getUsername(), company.getEmail())).thenReturn(Optional.of(company));

        assertThrows(UserFoundException.class, () -> createCompanyUseCase.execute(company));
    }

    @Test
    @DisplayName("Should throw UserFoundException when email is already in use")
    void should_throw_user_found_exception_when_email_is_already_in_use() {
        CompanyEntity company = CompanyEntity.builder()
                .id(UUID.randomUUID())
                .username("company")
                .email("company@example.com")
                .password("password")
                .build();

        when(companyRepository.findByUsernameOrEmail(company.getUsername(), company.getEmail())).thenReturn(Optional.empty());
        when(companyRepository.save(company)).thenReturn(company);

        createCompanyUseCase.execute(company);

        CompanyEntity companyWithSameEmail = CompanyEntity.builder()
                .id(UUID.randomUUID())
                .username("anothercompany")
                .email("company@example.com") // Mesmo e-mail
                .password("password")
                .build();
        when(companyRepository.findByUsernameOrEmail(any(), eq(company.getEmail()))).thenReturn(Optional.of(companyWithSameEmail));

        assertThrows(UserFoundException.class, () -> createCompanyUseCase.execute(companyWithSameEmail));
    }
}