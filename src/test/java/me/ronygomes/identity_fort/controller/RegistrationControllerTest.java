package me.ronygomes.identity_fort.controller;

import me.ronygomes.identity_fort.entity.User;
import me.ronygomes.identity_fort.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.hamcrest.Matchers;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {

    private static final String REGISTRATION_COMMAND_NAME = "user";
    private static final String FORGOT_PASSWORD_COMMAND_NAME = "emailCmd";

    @Mock
    UserService userService;

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
}
