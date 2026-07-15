package de.mreinisch.backend.controller;

import de.mreinisch.backend.model.AppUser;
import de.mreinisch.backend.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @GetMapping("/user")
    public AppUser getAppUser(@AuthenticationPrincipal OAuth2User oAuth2User){
        return service.getAuthUser(oAuth2User);
    }
}
