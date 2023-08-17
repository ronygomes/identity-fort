package me.ronygomes.identity_fort.controller;

import me.ronygomes.identity_fort.entity.User;
import me.ronygomes.identity_fort.entity.VerificationToken;
import me.ronygomes.identity_fort.repository.UserRepository;
import me.ronygomes.identity_fort.repository.VerificationTokenRepository;
import me.ronygomes.identity_fort.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {

    private static final String REGISTRATION_COMMAND_NAME = "user";
    private static final String FORGOT_PASSWORD_COMMAND_NAME = "emailCmd";

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    VerificationTokenRepository verificationTokenRepository;

    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    RegistrationController controller;

    MockMvc mockMvc;
    InternalResourceViewResolver viewResolver;

    @BeforeEach
    void setUp() {
        // If viewResolver is not configured, throws error when viewName and requestUrl is same
        // Another option is to load the ApplicationContext, but overkill for this situation
        // Sample Error Message: Circular view path [forgetPassword]: would dispatch back to the current handler URL [/forgetPassword] again.
        viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void testRegistrationGetView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attribute(REGISTRATION_COMMAND_NAME,
                        Matchers.hasProperty("id", Matchers.equalTo(0))))
                .andExpect(MockMvcResultMatchers.view().name("registration"));
    }

    @Test
    void testRegistrationPostWithEmptyRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register"))
                .andExpect(MockMvcResultMatchers.model().errorCount(3))
                .andExpect(MockMvcResultMatchers.model()
                        .attributeHasFieldErrors(REGISTRATION_COMMAND_NAME, "email", "firstName", "lastName"))
                .andExpect(MockMvcResultMatchers.view().name("registration"));
    }

    @Test
    void testRegistrationPostWithValidRequest() throws Exception {
        ArgumentCaptor<User> acUser = ArgumentCaptor.forClass(User.class);
        Mockito.doNothing().when(userService).registerUser(acUser.capture(), Mockito.notNull());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com"))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successRedirectMessage"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));

        User u = acUser.getValue();
        Assertions.assertEquals("John", u.getFirstName());
        Assertions.assertEquals("Doe", u.getLastName());
        Assertions.assertEquals("john@example.com", u.getEmail());
    }

    @Test
    void testRegistrationInitBinding() throws Exception {
        ArgumentCaptor<User> acUser = ArgumentCaptor.forClass(User.class);
        Mockito.doNothing().when(userService).registerUser(acUser.capture(), Mockito.notNull());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("id", "123")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com")
                        .param("rawPassword", "password1")
                        .param("confirmPassword", "password2")
                        .param("registrationDate", "12/10/2023")
                        .param("locked", "true")
                        .param("enabled", "true"))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successRedirectMessage"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));

        User u = acUser.getValue();
        Assertions.assertEquals("John", u.getFirstName());
        Assertions.assertEquals("Doe", u.getLastName());
        Assertions.assertEquals("john@example.com", u.getEmail());
        Assertions.assertEquals("password1", u.getRawPassword());
        Assertions.assertEquals("password2", u.getConfirmPassword());

        Assertions.assertEquals(0, u.getId());
        Assertions.assertNull(u.getRegistrationDate());
        Assertions.assertFalse(u.isLocked());
        Assertions.assertFalse(u.isEnabled());
    }

    @Test
    void testForgetPasswordGetView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/forgetPassword"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attribute(FORGOT_PASSWORD_COMMAND_NAME,
                        Matchers.hasProperty("email", Matchers.nullValue())))
                .andExpect(MockMvcResultMatchers.view().name("forgetPassword"));
    }

    @Test
    void testConfirmTokenGetWithExpiredToken() throws Exception {
        VerificationToken token = Mockito.mock(VerificationToken.class);
        Mockito.when(token.isExpired()).thenReturn(true);

        Mockito.when(verificationTokenRepository.getReferenceById("123-456")).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.get("/confirmToken/123-456"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errorRedirectMessage"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));
    }

    @Test
    void testConfirmTokenGetWithEmailConfirmation() throws Exception {
        User u = new User();
        u.setId(1);
        u.setEnabled(false);

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(u));

        VerificationToken token = Mockito.mock(VerificationToken.class);
        Mockito.when(token.isExpired()).thenReturn(false);
        Mockito.when(token.getType()).thenReturn(VerificationToken.TokenType.EMAIL_CONFIRMATION);
        Mockito.when(token.getOwner()).thenReturn(u);

        Mockito.when(verificationTokenRepository.getReferenceById("123-456")).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.get("/confirmToken/123-456"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successRedirectMessage"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));

        Mockito.verify(verificationTokenRepository).delete(token);
        Assertions.assertTrue(u.isEnabled());
    }

    @Test
    void testConfirmTokenGetWithForgetPassword() throws Exception {
        VerificationToken token = Mockito.mock(VerificationToken.class);
        Mockito.when(token.isExpired()).thenReturn(false);
        Mockito.when(token.getType()).thenReturn(VerificationToken.TokenType.FORGET_PASSWORD);

        Mockito.when(verificationTokenRepository.getReferenceById("123-456")).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.get("/confirmToken/123-456"))
                .andExpect(MockMvcResultMatchers.view().name("resetPassword"));
    }

    @Test
    void testConfirmTokenPostWithExpiredToken() throws Exception {
        VerificationToken token = Mockito.mock(VerificationToken.class);
        Mockito.when(token.isExpired()).thenReturn(true);

        Mockito.when(verificationTokenRepository.getReferenceById("123-456")).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmToken/123-456")
                        .param("password", "123")
                        .param("confirmPassword", "123"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errorRedirectMessage"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));
    }

    @Test
    void testConfirmTokenPostWithInvalidToken() throws Exception {
        VerificationToken token = Mockito.mock(VerificationToken.class);
        Mockito.when(token.isExpired()).thenReturn(false);
        Mockito.when(token.getType()).thenReturn(VerificationToken.TokenType.EMAIL_CONFIRMATION);

        Mockito.when(verificationTokenRepository.getReferenceById("123-456")).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmToken/123-456")
                        .param("password", "123")
                        .param("confirmPassword", "123"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("errorRedirectMessage"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));
    }

    @Test
    void testConfirmTokenPostWithPasswordMismatch() throws Exception {
        VerificationToken token = Mockito.mock(VerificationToken.class);
        Mockito.when(token.isExpired()).thenReturn(false);
        Mockito.when(token.getType()).thenReturn(VerificationToken.TokenType.FORGET_PASSWORD);

        Mockito.when(verificationTokenRepository.getReferenceById("123-456")).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmToken/123-456")
                        .param("password", "123")
                        .param("confirmPassword", "456"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"))
                .andExpect(MockMvcResultMatchers.view().name("resetPassword"));
    }

    @Test
    void testConfirmTokenPostWithNonMissingPasswordValue() throws Exception {
        VerificationToken token = Mockito.mock(VerificationToken.class);
        Mockito.when(token.isExpired()).thenReturn(false);
        Mockito.when(token.getType()).thenReturn(VerificationToken.TokenType.FORGET_PASSWORD);

        Mockito.when(verificationTokenRepository.getReferenceById("123-456")).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmToken/123-456"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage",
                        Matchers.is("Both fields are required")))
                .andExpect(MockMvcResultMatchers.view().name("resetPassword"));
    }

    @Test
    void testConfirmTokenPostWithValidValue() throws Exception {
        User u = new User();

        VerificationToken token = Mockito.mock(VerificationToken.class);
        Mockito.when(token.isExpired()).thenReturn(false);
        Mockito.when(token.getType()).thenReturn(VerificationToken.TokenType.FORGET_PASSWORD);
        Mockito.when(token.getOwner()).thenReturn(u);

        Mockito.when(verificationTokenRepository.getReferenceById("123-456")).thenReturn(token);
        Mockito.when(encoder.encode("123")).thenReturn("ABC");

        mockMvc.perform(MockMvcRequestBuilders.post("/confirmToken/123-456")
                        .param("password", "123")
                        .param("confirmPassword", "123"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successRedirectMessage"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));

        Mockito.verify(verificationTokenRepository).delete(token);
        Assertions.assertEquals("ABC", u.getHashedPassword());
    }

    @Test
    void testForgetPasswordPostWithMalformedEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/forgetPassword")
                        .param("email", "Malformed Email"))
                .andExpect(MockMvcResultMatchers.model()
                        .attributeHasFieldErrors(FORGOT_PASSWORD_COMMAND_NAME, "email"))
                .andExpect(MockMvcResultMatchers.view().name("forgetPassword"));
    }

    @Test
    void testForgetPasswordPostWithBlankEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/forgetPassword"))
                .andExpect(MockMvcResultMatchers.model()
                        .attributeHasFieldErrors(FORGOT_PASSWORD_COMMAND_NAME, "email"))
                .andExpect(MockMvcResultMatchers.view().name("forgetPassword"));
    }

    @Test
    void testForgetPasswordForValidRequest() throws Exception {
        User u = new User();
        Mockito.when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(u));

        ArgumentCaptor<VerificationToken> acToken = ArgumentCaptor.forClass(VerificationToken.class);
        Mockito.when(verificationTokenRepository.save(acToken.capture())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/forgetPassword")
                        .param("email", "john@example.com"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("successRedirectMessage"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/login"));

        VerificationToken token = acToken.getValue();
        Assertions.assertSame(u, token.getOwner());
        Assertions.assertNotNull(token.getToken());
        Assertions.assertSame(VerificationToken.TokenType.FORGET_PASSWORD, token.getType());
    }
}
