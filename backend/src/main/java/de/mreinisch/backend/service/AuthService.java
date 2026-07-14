package de.mreinisch.backend.service;

import de.mreinisch.backend.model.AppUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public AppUser getAuthUser(OAuth2User user){
        return new AppUser(user.getName(),
                           user.getAttributes().get("login").toString());
    }
}
