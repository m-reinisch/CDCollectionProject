package de.mreinisch.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void getAppUser_shouldReturnAppUser_whenLoggedInWithGoogle() throws Exception {
        mvc.perform(get("/api/auth/user")
                        .with(oidcLogin()
                                .idToken( token ->
                                        token.claim("sub", "TestId"))
                                .userInfoToken( token ->
                                        token.claim("name", "TestUser"))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                              {
                                "id": "TestId",
                                "username": "TestUser"
                              }
                            """));
    }

    @Test
    void getAppUser_shouldReturnAppUser_whenLoggedInWithGitHub() throws Exception {
        mvc.perform(get("/api/auth/user")
                        .with(oauth2Login()
                                .attributes( attribut -> {
                                        attribut.put("id", "TestId");
                                        attribut.put("login", "TestUser");
                                })
                        ))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                              {
                                "username": "TestUser"
                              }
                            """))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void getAppUser_shouldReturnsEmpty_whenUnauthenticated() throws Exception {
        mvc.perform(get("/api/auth/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
