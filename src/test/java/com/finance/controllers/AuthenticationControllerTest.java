package com.finance.controllers;

import io.jsonwebtoken.lang.Assert;
import org.json.JSONObject;
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

import java.net.URI;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    private final String URL = "/auth";
    private final String EMAIL_FIELD = "email";
    private final String PASSWORD_FIELD = "senha";

    @Test
    void whenUserAndEmailIsCorrect_thenSucess() throws Exception {
        URI uri = new URI(URL);
        String json = new JSONObject().put(EMAIL_FIELD, "sandro-matheus@hotmail.com")
                .put(PASSWORD_FIELD, "1234567")
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .is(HttpStatus.OK.value())).andReturn();
        String content = mvcResult.getRequest().getContentAsString();
        Assert.hasText("Bearer", content);
    }

    @Test
    void whenUserAndEmailIsIncorrect_thenUserNotFound() throws Exception {
        URI uri = new URI(URL);
        String json = new JSONObject().put(EMAIL_FIELD, "usuarionaoregistrado@hotmail.com")
                .put(PASSWORD_FIELD, "1senhaaleatoria4567")
                .toString();
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .is(HttpStatus.BAD_REQUEST.value()));
    }
}