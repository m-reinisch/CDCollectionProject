package de.mreinisch.backend.security;

import de.mreinisch.backend.model.AppUser;
import de.mreinisch.backend.repository.AppUserRepo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final AppUserRepo userRepo;

    public CustomOauth2UserService(AppUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        userRepo.findById(oAuth2User.getName())
                .orElseGet(() -> createUser(oAuth2User));

        return oAuth2User;
    }

    private AppUser createUser(OAuth2User newUser){
        AppUser temp = AppUser.builder()
                .id(newUser.getName())
                .username(newUser.getAttribute("login"))
                .build();

        userRepo.save(temp);
        return temp;
    }
}
