package me.ronygomes.identity_fort.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import me.ronygomes.identity_fort.advice.RestResponseEntityExceptionHandler;
import me.ronygomes.identity_fort.entity.User;
import me.ronygomes.identity_fort.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.TimeZone;

@ExtendWith(MockitoExtension.class)
public class UserResourceControllerTest {

    @Mock
    TokenStore tokenStore;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserResourceController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper om = new ObjectMapper();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        DateFormat df = new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        om.setDateFormat(df);

        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.setObjectMapper(om);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .setMessageConverters(jacksonConverter)
                .build();
    }

    @Test
    void testRequestWithoutAuthorizationHeader() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/info"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testRequestWithAuthorizationHeaderButNonExistentToken() throws Exception {
        Mockito.when(tokenStore.readAuthentication("589")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/info")
                        .header("Authorization", "Bearer 589")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void testRequestWithAuthorizationHeaderWithValidToken() throws Exception {
        User u = new User();
        u.setEmail("john@example.com");

        OAuth2Authentication authentication = Mockito.mock(OAuth2Authentication.class);

        Mockito.when(authentication.getPrincipal()).thenReturn(u);
        Mockito.when(tokenStore.readAuthentication("589")).thenReturn(authentication);

        User dbUser = createDummyUser();
        ;
        Mockito.when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(dbUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/info")
                        .header("Authorization", "Bearer 589")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationDate", Matchers.is("2021-06-30T09:43:07.340Z")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("john@example.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enabled", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.locked", Matchers.is(false)));
    }

    private User createDummyUser() throws ParseException {
        User u = new User();
        u.setId(1);
        u.setFirstName("John");
        u.setLastName("Doe");

        DateFormat df = new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        u.setRegistrationDate(df.parse("2021-06-30T09:43:07.340Z"));
        u.setEmail("john@example.com");
        u.setEnabled(true);
        u.setLocked(false);

        return u;
    }
}
