package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {
    @InjectMocks
    private TutorService service;

    @Mock
    private TutorRepository repository;

    @Mock
    private CadastroTutorDto cadastroTutorDto;

    private AtualizacaoTutorDto atualizacaoTutorDto;

    @Mock
    private Tutor tutor;

    @Captor
    private ArgumentCaptor<Tutor> tutorCaptor;

    @Captor
    private ArgumentCaptor<AtualizacaoTutorDto> tutorAtualizacaoCaptor;

    @Test
    void naoCadastrarTutorPoisJaCadastrado() {
        // ARRANGE + ACT
        BDDMockito.given(repository.existsByTelefoneOrEmail(cadastroTutorDto.telefone(), cadastroTutorDto.email()))
                .willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> service.cadastrar(cadastroTutorDto));
    }

    @Test
    void deveriaCadastrarTutor() {
        // ARRANGE
        this.cadastroTutorDto = new CadastroTutorDto(
            "Adriel",
            "11999999999",
            "adriel@gmail.com"
        );
        BDDMockito.given(repository.existsByTelefoneOrEmail(cadastroTutorDto.telefone(), cadastroTutorDto.email()))
                .willReturn(false);

        // ACT + ASSERT
        Assertions.assertDoesNotThrow(() -> service.cadastrar(cadastroTutorDto));
        BDDMockito.then(repository).should().save(tutorCaptor.capture());
        var tutorSalvo = tutorCaptor.getValue();
        Assertions.assertEquals(tutorSalvo.getNome(), cadastroTutorDto.nome());
        Assertions.assertEquals(tutorSalvo.getTelefone(), cadastroTutorDto.telefone());
        Assertions.assertEquals(tutorSalvo.getEmail(), cadastroTutorDto.email());
    }

    @Test
    void deveriaAtualizarTutor() {
        // ARRANGE
        this.atualizacaoTutorDto = new AtualizacaoTutorDto(
                1L,
                "Adriel",
                "11999999999",
                "adriel@gmail.com"
        );
        BDDMockito.given(repository.getReferenceById(atualizacaoTutorDto.id())).willReturn(tutor);

        // ACT
        service.atualizar(atualizacaoTutorDto);

        // ASSERT
        BDDMockito.then(tutor).should().atualizarDados(tutorAtualizacaoCaptor.capture());
        var tutorSalvo = tutorAtualizacaoCaptor.getValue();
        Assertions.assertEquals(tutorSalvo.id(), atualizacaoTutorDto.id());
        Assertions.assertEquals(tutorSalvo.nome(), atualizacaoTutorDto.nome());
        Assertions.assertEquals(tutorSalvo.telefone(), atualizacaoTutorDto.telefone());
        Assertions.assertEquals(tutorSalvo.email(), atualizacaoTutorDto.email());
    }

}