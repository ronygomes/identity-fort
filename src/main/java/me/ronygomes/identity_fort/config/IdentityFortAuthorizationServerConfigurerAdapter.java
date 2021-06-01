package me.ronygomes.identity_fort.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class IdentityFortAuthorizationServerConfigurerAdapter extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder encoder;
    private final DataSource dataSource;
    private final AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    public IdentityFortAuthorizationServerConfigurerAdapter(PasswordEncoder encoder,
                                                            DataSource dataSource,
                                                            @Qualifier("identityFortAuthorizationServerTokenServices")
                                                                    AuthorizationServerTokenServices authorizationServerTokenServices) {
        this.encoder = encoder;
        this.dataSource = dataSource;
        this.authorizationServerTokenServices = authorizationServerTokenServices;
    }

    // http://localhost:8081/oauth/token
    // http://localhost:8081/oauth/authorize
    // http://localhost:8080/oauth/authorize?response_type=code&client_id=testClient&redirect_uri=https%3A%2F%2Flocalhost%2Fcallback&&scope=user_info&state=1
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("testClient")
                .secret(encoder.encode("12345"))
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("user_info")
                .autoApprove(false)
                .redirectUris("https://localhost/callback");

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenServices(authorizationServerTokenServices);
    }
}
