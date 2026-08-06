package de.mreinisch.backend.service;

import de.mreinisch.backend.model.AppUser;
import de.mreinisch.backend.security.CustomOauth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public AppUser getAuthUser(OAuth2User user){
        if (user == null) {
            return null;
        }
        if (CustomOauth2UserService.isGoogle(user)) {
            return new AppUser(
                    user.getAttributes().get("sub").toString(),
                    user.getAttributes().get("name").toString());
        } else {
            return new AppUser(
                    user.getName(),
                    user.getAttributes().get("login").toString());
        }
    }
}
