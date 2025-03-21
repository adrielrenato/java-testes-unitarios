package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.service.TutorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TutorControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CadastroTutorDto> jsonDto;

    @MockBean
    private TutorService tutorService;

    @Test
    void deveriaRetornarCodigo200Cadastrar() throws Exception {
        // ARRANGE
        var json = """
                {
                    "nome": "Fulano da Silva",
                    "telefone": "61900000000",
                    "email": "fulano@email.com.br"
                }
                """;

        // ACT
        var response = mvc.perform(
            post("/tutores")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaRetornarCodigo400Cadastrar() throws Exception {
        // ARRANGE
        var json = "{}";

        // ACT
        var response = mvc.perform(
                post("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaRetornarCodigo200Atualizar() throws Exception {
        // ARRANGE
        var json = """
                {
                    "id": 1,
                    "nome": "Fulano da Silva 2",
                    "telefone": "61900000001",
                    "email": "fulano2@email.com.br"
                }
                """;

        // ACT
        var response = mvc.perform(
                put("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaRetornarCodigo400Atualizar() throws Exception {
        // ARRANGE
        var json = "{}";

        // ACT
        var response = mvc.perform(
                put("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }
}