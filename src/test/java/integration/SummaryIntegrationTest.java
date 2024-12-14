package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.iam.application.AccessManagementFacade;
import personal.finance.tracking.summary.application.SummaryFacade;
import personal.finance.tracking.summary.domain.Summary;

import java.util.UUID;

import static integration.SummaryAssert.assertThatSummary;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static personal.finance.Fixtures.USER_ID;


@Transactional
public class SummaryIntegrationTest extends IntegrationTest {

    @Autowired
    private AccessManagementFacade accessManagementFacade;
    @Autowired
    private SummaryFacade summaryFacade;

    @BeforeEach
    void setUp() {
        summaryFacade.getConfirmedSummaries(UUID.fromString(USER_ID)).forEach(
            summary -> summaryFacade.cancelSummary(summary.getId().getValue(), UUID.fromString(USER_ID))
        );
    }

    @Test
    public void shouldCreateSummary() throws Exception {
        // given
        String token = accessManagementFacade.login("user@gmail.com", "123").token();
        // when
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/" + USER_ID + "/summaries/new")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType("application/json")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        // then
        String expected = getFile("summaries/should_create_summary.json");
        assertThatSummary(result)
            .isEqualTo(expected)
            .idIsUUID()
            .dateIsFormatted();
    }

    @Test
    public void shouldConfirmSummary() throws Exception {
        // given
        String token = accessManagementFacade.login("user@gmail.com", "123").token();
        Summary newSummary = summaryFacade.createNewSummary(UUID.fromString(USER_ID));

        // when
        String result = mockMvc.perform(
                MockMvcRequestBuilders.post("/" + USER_ID + "/summaries/" + newSummary.getId() + "/confirm")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType("application/json")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        // then
        String expected = getFile("summaries/should_confirm_summary.json");
        assertThatSummary(result)
            .isEqualTo(expected)
            .idIsUUID()
            .dateIsFormatted();
    }

    @Test
    public void shouldReturnCurrentDraft() throws Exception {
        // given
        String token = accessManagementFacade.login("user@gmail.com", "123").token();
        summaryFacade.createNewSummary(UUID.fromString(USER_ID));

        // when
        String result = mockMvc.perform(
                MockMvcRequestBuilders.get("/" + USER_ID + "/summaries/current")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType("application/json")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        // then
        String expected = getFile("summaries/should_return_current_draft.json");
        assertThatSummary(result)
            .isEqualTo(expected)
            .idIsUUID()
            .dateIsFormatted();
    }
}
