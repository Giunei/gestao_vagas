package br.com.giunei.gestao_vagas.modules.candidate.use_cases;

import br.com.giunei.gestao_vagas.exceptions.UserFoundException;
import br.com.giunei.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.giunei.gestao_vagas.modules.candidate.dto.CandidateDTO;
import br.com.giunei.gestao_vagas.modules.candidate.entity.CandidateEntity;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCandidateUseCaseTest {

    @InjectMocks
    private CreateCandidateUseCase createCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should create a new candidate")
    void should_create_a_new_candidate() {
        // Arrange
        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setId(UUID.randomUUID());
        candidateDTO.setUsername("john_doe");
        candidateDTO.setEmail("john.doe@example.com");
        candidateDTO.setPassword("password");
        candidateDTO.setName("John Doe");
        candidateDTO.setLinkedInURL("https://www.linkedin.com/in/johndoe/");
        candidateDTO.setDescription("Software Developer");

        CandidateEntity candidateEntity = CandidateEntity.builder()
                .id(candidateDTO.getId())
                .username(candidateDTO.getUsername())
                .email(candidateDTO.getEmail())
                .password("encodedPassword") // Assuming password encoder will encode the password
                .name(candidateDTO.getName())
                .linkedInURL(candidateDTO.getLinkedInURL())
                .description(candidateDTO.getDescription())
                .build();

        when(candidateRepository.findByUsernameOrEmail(candidateDTO.getUsername(), candidateDTO.getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(candidateDTO.getPassword())).thenReturn("encodedPassword");
        when(candidateRepository.save(any(CandidateEntity.class))).thenReturn(candidateEntity);

        // Act
        CandidateEntity result = createCandidateUseCase.execute(candidateDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(candidateDTO.getId());
        assertThat(result.getUsername()).isEqualTo(candidateDTO.getUsername());
        assertThat(result.getEmail()).isEqualTo(candidateDTO.getEmail());
        assertThat(result.getName()).isEqualTo(candidateDTO.getName());
        assertThat(result.getLinkedInURL()).isEqualTo(candidateDTO.getLinkedInURL());
        assertThat(result.getDescription()).isEqualTo(candidateDTO.getDescription());
    }

    @Test
    @DisplayName("Should throw UserFoundException when username or email already exists")
    void should_throw_user_found_exception_when_username_or_email_already_exists() {
        // Arrange
        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setUsername("existing_username");
        candidateDTO.setEmail("existing_email@example.com");

        when(candidateRepository.findByUsernameOrEmail(candidateDTO.getUsername(), candidateDTO.getEmail()))
                .thenReturn(Optional.of(new CandidateEntity()));

        // Act and Assert
        assertThrows(UserFoundException.class, () -> createCandidateUseCase.execute(candidateDTO));
    }
}
