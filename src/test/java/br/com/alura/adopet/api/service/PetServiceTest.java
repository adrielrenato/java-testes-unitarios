package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    private PetService service;

    @Mock
    private PetRepository repository;

    @Mock
    private AbrigoService abrigoService;

    @Mock
    private Abrigo abrigo;

    private CadastroPetDto dto;

    @Captor
    private ArgumentCaptor<Pet> petCaptor;

    @Test
    void deveriaBuscarTodosPetsDisponiveis() {
        // ACT
        service.buscarPetsDisponiveis();

        // ASSERT
        BDDMockito.then(repository).should().findAllByAdotadoFalse();
    }

    @Test
    void deveriaCadastrarPet() {
        // ARRANGE
        this.dto = new CadastroPetDto(
            TipoPet.GATO,
            "Miau",
            "Siames",
            4,
            "Cinza",
            4.0f
        );

        // ACT
        service.cadastrarPet(abrigo, dto);

        // ASSERT
        BDDMockito.then(repository).should().save(petCaptor.capture());
        var petSalvo = petCaptor.getValue();
        Assertions.assertEquals(petSalvo.getAbrigo(), abrigo);
        Assertions.assertEquals(petSalvo.getTipo(), dto.tipo());
        Assertions.assertEquals(petSalvo.getNome(), dto.nome());
        Assertions.assertEquals(petSalvo.getRaca(), dto.raca());
        Assertions.assertEquals(petSalvo.getIdade(), dto.idade());
        Assertions.assertEquals(petSalvo.getCor(), dto.cor());
        Assertions.assertEquals(petSalvo.getPeso(), dto.peso());
    }
}