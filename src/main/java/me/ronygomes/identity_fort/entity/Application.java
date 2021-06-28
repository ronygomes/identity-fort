package me.ronygomes.identity_fort.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

public class Application implements ClientDetails {

    private static final int ACCESS_TOKEN_VALIDITY_1_HOUR_IN_SECOND = 60 * 60;
    private static final int REFRESH_TOKEN_VALIDITY_30_DAYS_IN_SECOND = 10 * 24 * 60 * 60;

    private static final Set<String> DEFAULT_GRANT_TYPES =
            new HashSet<>(asList("authorization_code", "refresh_token"));

    private static final Set<String> ALLOWED_SCOPES = new HashSet<>(singleton("user_info"));

    private int id;

    private String clientId;
    private String clientSecret;
    private Set<String> registeredRedirectUri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUri) {
        this.registeredRedirectUri = registeredRedirectUri;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return new HashSet<>();
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return ALLOWED_SCOPES;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return DEFAULT_GRANT_TYPES;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return ACCESS_TOKEN_VALIDITY_1_HOUR_IN_SECOND;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return REFRESH_TOKEN_VALIDITY_30_DAYS_IN_SECOND;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return new HashMap<>();
    }
}
