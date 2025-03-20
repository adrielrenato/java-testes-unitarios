package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComAdocaoEmAndamentoTest {

    @InjectMocks
    private ValidacaoTutorComAdocaoEmAndamento validacao;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Spy
    private Tutor tutor = new Tutor();

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Spy
    private Adocao adocaoEmAndamento = new Adocao();

    @Test
    void deveriaPermitirSolicitacaoDePetTutorNaoTemAdocaoEmAndamento() {
        // ARRANGE
        BDDMockito.given(adocaoRepository.findAll()).willReturn(List.of(adocaoEmAndamento));
        BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);

        // ACT + ASSERT
        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void naoDeveriaPermitirSolicitacaoPetTutorComAdocaoEmAndamento() {
        // ARRANGE
        BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        BDDMockito.given(adocaoRepository.findAll()).willReturn(List.of(adocaoEmAndamento));

        Mockito.doReturn(tutor).when(adocaoEmAndamento).getTutor();
        Mockito.doReturn(StatusAdocao.AGUARDANDO_AVALIACAO).when(adocaoEmAndamento).getStatus();

        // ACT + ASSERT
        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }
}