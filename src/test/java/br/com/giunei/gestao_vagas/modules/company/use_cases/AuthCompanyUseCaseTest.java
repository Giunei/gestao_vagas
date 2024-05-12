package br.com.giunei.gestao_vagas.modules.company.use_cases;

import br.com.giunei.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.giunei.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthCompanyUseCaseTest {

    @InjectMocks
    private AuthCompanyUseCase authCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("Should throw UsernameNotFoundException when company is not found")
    void should_throw_username_not_found_exception_when_company_is_not_found() {
        String username = "company";
        String password = "password";

        AuthCompanyDTO authCompanyDTO = AuthCompanyDTO.builder()
                .username(username)
                .password(password)
                .build();

        when(companyRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authCompanyUseCase.execute(authCompanyDTO));
    }
}