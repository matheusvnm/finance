package com.finance.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.config.security.JwtService;
import com.finance.dto.TokenDto;
import com.finance.repository.DespesaRepository;
import io.jsonwebtoken.lang.Assert;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServlet;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DespesaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private JwtService jwtService;

    private String token;
    private Long usuarioId;
    private final String URL = "/despesas";


    @BeforeEach
    void setUp() throws Exception {
        URI uri = new URI("/auth");
        String json = new JSONObject().put("email", "sandro-matheus@hotmail.com")
                .put("senha", "1234567")
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String returnedJson = mvcResult.getResponse().getContentAsString();
        TokenDto tokenDto = new ObjectMapper().readValue(returnedJson, TokenDto.class);
        token = tokenDto.getTipoToken() + " " + tokenDto.getToken();
        usuarioId = jwtService.getIdUsuario(tokenDto.getToken());
    }


    @Test
    void listarTodasDespesas() {
    }

    @Test
    void listarTodasDespesasPorMesAno() {
    }

    @Test
    void detalharDespesa() {
    }

    @Test
    @Transactional
    void whenCreatingDespesa_thenSucess() throws Exception {
        URI uri = new URI(URL);
        String json = new JSONObject().put("valor", 300)
                .put("descricao", "Registro da Agua")
                .put("data", "10/10/2020")
                .put("categoria", "MORADIA").toString();
        despesaRepository.deleteAllByUsuarioId(usuarioId);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(json)
                .header("Authorization", token)
                .contentType(
                        MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(
                HttpStatus.CREATED.value()));
    }

    @Test
    void whenCreatingTwoDespesasWithSameDescriptionInTheSameMonth_thenError() {

    }

    @Test
    void atualizaDespesa() {
    }

    @Test
    void deleteReceita() {
    }
}