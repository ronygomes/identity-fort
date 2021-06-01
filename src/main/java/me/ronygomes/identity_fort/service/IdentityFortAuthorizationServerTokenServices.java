package me.ronygomes.identity_fort.service;

import me.ronygomes.identity_fort.controller.RegistrationController;
import me.ronygomes.identity_fort.entity.UserAuthorizedClientAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;

@Service
public class IdentityFortAuthorizationServerTokenServices implements AuthorizationServerTokenServices {

    private static final Logger log = LoggerFactory.getLogger(IdentityFortAuthorizationServerTokenServices.class);

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        OAuth2Request req = authentication.getOAuth2Request();

        log.error("Authorities: {}", req.getAuthorities());
        log.error("Scope: {}", req.getScope());
        log.error("Client Id: {}", req.getClientId());
        log.error("Grant Types: {}", req.getGrantType());
        log.error("Redirect URL: {}", req.getRedirectUri());

        return new UserAuthorizedClientAccessToken();
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshToken, TokenRequest tokenRequest) throws AuthenticationException {
        return null;
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2Request req = authentication.getOAuth2Request();

        log.error("Authorities: {}", req.getAuthorities());
        log.error("Scope: {}", req.getScope());
        log.error("Client Id: {}", req.getClientId());
        log.error("Grant Types: {}", req.getGrantType());
        log.error("Redirect URL: {}", req.getRedirectUri());

        return null;
    }
}
