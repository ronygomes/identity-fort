package me.ronygomes.identity_fort.config;

import me.ronygomes.identity_fort.service.IdentityFortUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@EnableWebSecurity
public class IdentityFortWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final IdentityFortUserDetailsService userDetailsService;

    @Autowired
    public IdentityFortWebSecurityConfigurerAdapter(DataSource dataSource,
                                                    IdentityFortUserDetailsService userDetailsService) {

        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/register", "/forgetPassword", "/confirmToken/*").permitAll()
                .antMatchers("/oauth/token", "/oauth/authorize").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .usernameParameter("email")
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .permitAll()
                .defaultSuccessUrl("/home")

                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()

                .and()
                .rememberMe()
                .tokenRepository(tokenRepository());

//                .and()
//                .mvcMatcher("/v1/users/**").authorizeRequests()
//                .mvcMatchers("/v1/users/**").access("hasAuthority('SCOPE_user_info')")
//                .and()
//                .oauth2ResourceServer()
//                .jwt();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.mvcMatcher("/v1/users/**")
//                .authorizeRequests()
//                .mvcMatchers("/v1/users/**").access("hasAuthority('SCOPE_user_info')")
//                .and()
//                .oauth2ResourceServer()
//                .jwt();
//        return http.build();
//    }

    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}
