package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AbrigoControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AbrigoService abrigoService;

    @MockBean
    private PetService petService;

    @Mock
    private Abrigo abrigo;

    @Mock
    private CadastroPetDto dto;

    @Test
    void deveriaDevolverCodigo200ListarSemErros() throws Exception {
        // ACT
        var response = mvc.perform(
            get("/abrigos")
                    .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200CadastrarSemErros() throws Exception {
        // ARRANGE
        var json = """
                {
                    "nome": "Abrigo xpto",
                    "telefone": "61977777777",
                    "email": "abrigoxpto@email.com.br"
                }
                """;

        // ACT
        var response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400CadastrarComErros() throws Exception {
        // ARRANGE
        var json = "{}";

        // ACT
        var response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ListarPetsSemErros() throws Exception {
        // ARRANGE
        String nome = "Abrigo Feliz";

        // ACT
        var response = mvc.perform(
                get("/abrigos/{nome}/pets", nome)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ListarPetsNaoEncontrado() throws Exception {
        // ARRANGE
        String nome = "Abrigo Feliz";
        BDDMockito.given(abrigoService.listarPetsDoAbrigo(nome)).willThrow(ValidacaoException.class);

        // ACT
        var response = mvc.perform(
                get("/abrigos/{nome}/pets", nome)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ListarPetsComErros() throws Exception {
        // ARRANGE
        String nome = "Miau";
        BDDMockito.given(abrigoService.listarPetsDoAbrigo(nome)).willThrow(ValidacaoException.class);

        // ACT
        var response = mvc.perform(
                get("/abrigos/{nome}/pets", nome)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200CadastrarPetSemErros() throws Exception {
        // ARRANGE
        String nome = "Abrigo Feliz";
        var json = """
                {
                    "tipo": "CACHORRO",
                    "nome": "bidu",
                    "raca": "SRD",
                    "idade": 5,
                    "cor": "preto",
                    "peso": 5.45
                }
                """;
        BDDMockito.given(abrigoService.carregarAbrigo(nome)).willReturn(abrigo);

        // ACT
        var response = mvc.perform(
                post("/abrigos/{nome}/pets", nome)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400CadastrarPetComErros() throws Exception {
        // ARRANGE
        String nome = "Abrigo Feliz";
        var json = "{}";
        BDDMockito.given(abrigoService.carregarAbrigo(nome)).willThrow(ValidacaoException.class);

        // ACT
        var response = mvc.perform(
                post("/abrigos/{nome}/pets", nome)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404CadastrarPetNaoEncontrado() throws Exception {
        // ARRANGE
        String nome = "au au";
        var json = """
                {
                    "tipo": "CACHORRO",
                    "nome": "bidu",
                    "raca": "SRD",
                    "idade": 5,
                    "cor": "preto",
                    "peso": 5.45
                }
                """;
        BDDMockito.given(abrigoService.carregarAbrigo(nome)).willThrow(ValidacaoException.class);

        // ACT
        var response = mvc.perform(
                post("/abrigos/{nome}/pets", nome)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }
}