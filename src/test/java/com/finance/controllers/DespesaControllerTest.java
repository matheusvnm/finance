package com.finance.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.finance.config.security.JwtService;
import com.finance.domain.Despesa;
import com.finance.dto.TokenDto;
import com.finance.enums.CategoriaEnum;
import com.finance.form.DespesaForm;
import com.finance.repository.DespesaRepository;
import io.jsonwebtoken.lang.Assert;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
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
    private final URI uri = new URI("/despesas");

    DespesaControllerTest() throws URISyntaxException {
    }


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
        String returnedJson = mvcResult.getResponse()
                .getContentAsString();
        TokenDto tokenDto = new ObjectMapper().readValue(returnedJson, TokenDto.class);
        token = tokenDto.getTipoToken() + " " + tokenDto.getToken();
        usuarioId = jwtService.getIdUsuario(tokenDto.getToken());
        despesaRepository.deleteAllByUsuarioId(usuarioId);
    }


    @Test
    void whenlistarTodasDespesas_thenSuccess() throws Exception {
        sendDespesaFormAndGetId("Pagamento da Água", "10/05/2022",
                109.50, CategoriaEnum.MORADIA);
        sendDespesaFormAndGetId("Pagamento da Internet", "10/05/2022",
                109.5, CategoriaEnum.MORADIA);
        sendDespesaFormAndGetId("Cartão Nubank", "10/06/2022",
                1200, CategoriaEnum.OUTROS);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(uri)
                                .header("Authorization", token)
                                .contentType(
                                        MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .is(HttpStatus.OK.value()))
                .andReturn();
        String content = mvcResult.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        System.out.println(content);
        Assert.isTrue(content.contains("Cartão Nubank") &&
                        content.contains("Internet") &&
                        content.contains("Água"));

    }

    @Test
    void whenlistarTodasDespesasPorMesAno_thenSuccess() throws Exception {
        sendDespesaFormAndGetId("Pagamento da Água", "10/05/2022",
                109.50, CategoriaEnum.MORADIA);
        sendDespesaFormAndGetId("Pagamento da Internet", "10/05/2022",
                109.5, CategoriaEnum.MORADIA);
        sendDespesaFormAndGetId("Cartão Nubank", "10/06/2022",
                1200, CategoriaEnum.OUTROS);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(uri + "/2022/06")
                                .header("Authorization", token)
                                .contentType(
                                        MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .is(HttpStatus.OK.value()))
                .andReturn();
        Assert.isTrue(mvcResult.getResponse()
                .getContentAsString(StandardCharsets.UTF_8)
                .contains("Cartão Nubank"));
    }

    @Test
    void whenDetalharDespesa_thenSuccess() throws Exception {
        Long usuarioId = sendDespesaFormAndGetId("Pagamento da Água", "10/05/2022",
                109.50, CategoriaEnum.MORADIA);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(uri + "/" + usuarioId.toString())
                                .header("Authorization", token)
                                .contentType(
                                        MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .is(HttpStatus.OK.value()))
                .andReturn();
        Assert.isTrue(mvcResult.getResponse()
                .getContentAsString(StandardCharsets.UTF_8)
                .contains("Pagamento da Água"));
    }

    @Test
    void whenCreatingDespesa_thenSucess() throws Exception {
        DespesaForm despesaForm = createDespesaForm("Registro da Agua", "10/10/2020",
                300, CategoriaEnum.MORADIA);
        String json = transformDespesaToJson(despesaForm);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(json)
                        .header("Authorization", token)
                        .contentType(
                                MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .is(
                                HttpStatus.CREATED.value()));
    }

    private DespesaForm createDespesaForm(String descricao, String data, double valor,
                                          CategoriaEnum categoria) {
        DespesaForm despesaForm = new DespesaForm();
        despesaForm.setCategoria(categoria);
        despesaForm.setValor(valor);
        despesaForm.setDescricao(descricao);
        despesaForm.setData(data);
        return despesaForm;
    }


    private String transformDespesaToJson(DespesaForm despesaForm) throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer()
                .withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(despesaForm);
    }

    @Test
    void whenCreatingTwoDespesasWithSameDescriptionInTheSameMonth_thenError() throws Exception {
        DespesaForm despesaForm = createDespesaForm("Pagamento da Luz", "11/10/2020",
                150.32, CategoriaEnum.OUTROS);
        String json = transformDespesaToJson(despesaForm);
        sendDespesaFormAndGetId("Pagamento da Luz", "23/10/2020",
                60.0, CategoriaEnum.ALIMENTACAO);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(json)
                        .header("Authorization", token)
                        .contentType(
                                MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .is(
                                HttpStatus.BAD_REQUEST.value()));
    }

    private Long sendDespesaFormAndGetId(String descricao, String data, double valor,
                                         CategoriaEnum categoriaEnum) throws Exception {
        DespesaForm despesaForm = createDespesaForm(descricao, data, valor,
                categoriaEnum);
        String json = transformDespesaToJson(despesaForm);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(json)
                .header("Authorization", token)
                .contentType(
                        MediaType.APPLICATION_JSON));
        return findDespesaId(data, descricao);
    }

    private Long findDespesaId(String data, String descricao) {
        LocalDate localDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Despesa despesa = despesaRepository.findFirstByDescricaoAndData(descricao, localDate);
        return despesa.getId();
    }

    @Test
    void whenAtualizaDespesa_thenSuccess() throws Exception {
        String descricao = "Padaria Rosariense";
        String data = "10/06/2022";
        Long despesaId = sendDespesaFormAndGetId(descricao, data, 33, CategoriaEnum.OUTROS);
        DespesaForm despesaFormAtualizado = createDespesaForm(descricao, "10/06/2022",
                45.2, CategoriaEnum.IMPREVISTOS);
        String json = transformDespesaToJson(despesaFormAtualizado);
        mockMvc.perform(MockMvcRequestBuilders.put(uri + "/" + despesaId.toString())
                        .content(json)
                        .header("Authorization", token)
                        .contentType(
                                MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .is(
                                HttpStatus.OK.value()));
    }


    @Test
    void whenDeleteDespesa_thenSucess() throws Exception {
        Long despesaId = sendDespesaFormAndGetId("RU", "10/12/2021", 50.2,
                CategoriaEnum.ALIMENTACAO);
        mockMvc.perform(MockMvcRequestBuilders.delete(uri + "/" + despesaId.toString())
                        .header("Authorization", token)
                        .contentType(
                                MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .is(
                                HttpStatus.OK.value()));
    }

    @AfterEach
    void tearDown(){
        despesaRepository.deleteAllByUsuarioId(usuarioId);
    }

}