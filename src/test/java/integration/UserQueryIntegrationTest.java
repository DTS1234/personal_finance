package integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import personal.finance.iam.application.AccessManagementFacade;
import personal.finance.iam.application.dto.AuthResponseDTO;
import personal.finance.iam.application.dto.UserRegistrationDTO;

import java.time.LocalDate;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserQueryIntegrationTest extends IntegrationTest {

    @Autowired
    private AccessManagementFacade accessManagementFacade;
    private static final String USER_ID = "66ea2da5-841c-4d16-9838-25cd2634af09";

    @Test
    public void shouldReturnSubscriptionForAUser() throws Exception {
        AuthResponseDTO login = accessManagementFacade.login("user@gmail.com", "123");
        String token = login.token();
        String userId = login.id();

        // when
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/"+userId+"/subscription")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        // then
        String expected = getFile("iam/should_return_subscription.json");
        assertThatJson(result)
            .whenIgnoringPaths("userId", "start", "expiresAt")
            .isEqualTo(expected);
    }
}
