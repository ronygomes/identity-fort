package me.ronygomes.identity_fort.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

@Service
public class IdentityFortClientDetailsService implements ClientDetailsService {

    private static final Logger log = LoggerFactory.getLogger(IdentityFortClientDetailsService.class);


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.error("Was Here");
//        Application application = new Application();
//
//        application.setClientId("testClient");
//        application.setClientSecret(new BCryptPasswordEncoder().encode("abc"));
//        application.setRegisteredRedirectUri(new HashSet<>(singleton("https://localhost/callback")));

        BaseClientDetails b = new BaseClientDetails("testClient", null,
                "user_info", null, null, "https://localhost/callback");

        b.setClientSecret(new BCryptPasswordEncoder().encode("abc"));
        return b;
//
//
//        return c;
    }
}
