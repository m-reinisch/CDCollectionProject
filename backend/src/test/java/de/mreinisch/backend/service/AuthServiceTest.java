package de.mreinisch.backend.service;

import de.mreinisch.backend.model.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Test
    void getAuthUser_shouldReturnAppUser_whenGoogleLogin() {
        String id= "0";
        OAuth2User oAuth2User= new DefaultOAuth2User(
                List.of(),
                Map.of("sub", id, "name", "TestUser"),
                "sub"
        );
        AuthService service= new AuthService();
        AppUser expected= new AppUser(id, "TestUser");
        AppUser actual;

        actual= service.getAuthUser(oAuth2User);
        assertEquals(expected, actual);
    }

    @Test
    void getAuthUser_shouldReturnAppUser_whenGitHubLogin() {
        String id= "0";
        OAuth2User oAuth2User= new DefaultOAuth2User(
                List.of(),
                Map.of("id", id, "login", "TestUser"),
                "id"
        );
        AuthService service= new AuthService();
        AppUser expected= new AppUser(id, "TestUser");
        AppUser actual;

        actual= service.getAuthUser(oAuth2User);
        assertEquals(expected, actual);
    }

    @Test
    void getAuthUser_shouldReturnNull_whenNotLoggedIn() {
        AuthService service= new AuthService();
        AppUser expected= null;
        AppUser actual;

        actual= service.getAuthUser(null);
        assertEquals(expected, actual);
    }
}
