package me.ronygomes.identity_fort.controller;

import me.ronygomes.identity_fort.entity.User;
import me.ronygomes.identity_fort.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static org.springframework.security.oauth2.common.OAuth2AccessToken.BEARER_TYPE;

@RestController
@RequestMapping("/v1/users")
public class UserResourceController {

    private static final Logger log = LoggerFactory.getLogger(UserResourceController.class);

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private final UserRepository userRepository;
    private final TokenStore tokenStore;

    @Autowired
    public UserResourceController(UserRepository userRepository, TokenStore tokenStore) {
        this.userRepository = userRepository;
        this.tokenStore = tokenStore;
    }

    @GetMapping("/info")
    public User fetchInfo(HttpServletRequest request) {
        String headerValue = request.getHeader(AUTHORIZATION_HEADER_NAME);
        if (Objects.isNull(headerValue)) {
            throw new IllegalStateException();
        }

        String token = headerValue.substring(BEARER_TYPE.length()).trim();
        log.debug("Access Token: {}", token);

        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
        if (Objects.isNull(oAuth2Authentication)) {
            throw new IllegalStateException();
        }

        User tokenUser = (User) oAuth2Authentication.getPrincipal();
        return userRepository.findByEmail(tokenUser.getEmail()).orElseThrow(IllegalAccessError::new);
    }
}
