package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @InjectMocks
    private AbrigoService service;

    @Mock
    private AbrigoRepository repository;

    @Mock
    private PetRepository petRepository;

    @Spy
    private Abrigo abrigo = new Abrigo();

    private CadastroAbrigoDto dto;

    @Captor
    private ArgumentCaptor<Abrigo> abrigoCaptor;

    @Test
    void deveriaChamarListarTodosAbrigos() {
        // ACT
        service.listar();

        // ASSERT
        BDDMockito.then(repository).should().findAll();
    }

    @Test
    void deveriaCadastrarAbrigo() {
        // ARRANGE
        this.dto = new CadastroAbrigoDto(
            "Abrigo Feliz",
            "11999999999",
            "abrigofeliz@email.com"
        );

        // ACT
        service.cadatrar(dto);

        // ASSERT
        BDDMockito.then(repository).should().save(abrigoCaptor.capture());
        var abrigoSalvo = abrigoCaptor.getValue();
        Assertions.assertEquals(dto.nome(), abrigoSalvo.getNome());
        Assertions.assertEquals(dto.telefone(), abrigoSalvo.getTelefone());
        Assertions.assertEquals(dto.email(), abrigoSalvo.getEmail());
    }

    @Test
    void deveriaChamarListaDePetsDoAbrigoAtravesDoNome() {
        // ARRANGE
        String nome = "Miau";
        BDDMockito.given(repository.findByNome(nome)).willReturn(Optional.of(abrigo));

        // ACT
        service.listarPetsDoAbrigo(nome);

        // ASSERT
        BDDMockito.then(petRepository).should().findByAbrigo(abrigo);
    }
}