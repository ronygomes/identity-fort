package me.ronygomes.identity_fort.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

@Entity
@Table(name = "applications")
public class Application implements ClientDetails {

    private static final int ACCESS_TOKEN_VALIDITY_1_HOUR_IN_SECOND = 60 * 60;
    private static final int REFRESH_TOKEN_VALIDITY_30_DAYS_IN_SECOND = 10 * 24 * 60 * 60;

    private static final Set<String> DEFAULT_GRANT_TYPES =
            new HashSet<>(asList("authorization_code", "refresh_token"));

    private static final Set<String> ALLOWED_SCOPES = singleton("user_info");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "client_id", length = 100, nullable = false)
    private String clientId;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "client_secret", length = 100, nullable = false)
    private String clientSecret;

    @NotBlank
    @Size(min = 10, max = 200)
    @Column(name = "redirect_uri", length = 200, nullable = false)
    private String redirectUri;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, updatable = false)
    private Date createDate;

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

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        return singleton(redirectUri);
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
