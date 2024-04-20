package integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.iam.application.AccessManagementFacade;
import personal.finance.iam.application.dto.UserRegistrationDTO;

import java.time.LocalDate;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class AccessManagementIntegrationTest extends IntegrationTest {

    @Autowired
    private AccessManagementFacade accessManagementFacade;

    @Test
    public void shouldRegisterNewUser() throws Exception {
        // when
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new UserRegistrationDTO("new@gmail.com", "pass", LocalDate.of(1999, 7, 7), "Male", "Rodriguez", "Stefano"))))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        // then
        String expected = getFile("iam/should_register.json");
        assertThatJson(result)
            .whenIgnoringPaths("userInformation.password")
            .isEqualTo(expected);
    }

    @Test
    public void shouldConfirmUserRegistration() throws Exception {
        // given
        accessManagementFacade.registerUser(
            new UserRegistrationDTO("new@gmail.com", "pass", LocalDate.of(1999, 7, 7), "Male", "Rodriguez", "Stefano")
        );
        String verificationToken = getVerificationToken("new@gmail.com");
        // when
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/registration/confirm?token="+verificationToken)
                .contentType("application/json")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();
        // then
        String expected = getFile("iam/should_confirm.json");
        assertThatJson(result)
            .whenIgnoringPaths("userInformation.password")
            .isEqualTo(expected);
    }
}
