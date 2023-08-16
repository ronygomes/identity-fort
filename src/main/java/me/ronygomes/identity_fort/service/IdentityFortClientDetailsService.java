package me.ronygomes.identity_fort.service;

import me.ronygomes.identity_fort.entity.Application;
import me.ronygomes.identity_fort.repository.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdentityFortClientDetailsService implements ClientDetailsService {

    private static final Logger log = LoggerFactory.getLogger(IdentityFortClientDetailsService.class);

    private final ApplicationRepository applicationRepository;

    @Autowired
    public IdentityFortClientDetailsService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<Application> application = applicationRepository.findByClientId(clientId);
        if (application.isPresent()) {
            return new BaseClientDetails(application.get());
        }

        log.error("Unable to find client with id: " + clientId);
        throw new ClientRegistrationException("Unable to find client with id: " + clientId);
    }
}
