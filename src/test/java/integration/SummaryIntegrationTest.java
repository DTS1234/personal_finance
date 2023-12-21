package integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.iam.application.AccessManagementFacade;
import personal.finance.summary.application.SummaryFacade;
import personal.finance.summary.application.dto.DTOMapper;
import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;

import java.math.BigDecimal;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
public class SummaryIntegrationTest extends IntegrationTest {

    @Autowired
    private AccessManagementFacade accessManagementFacade;
    @Autowired
    private SummaryFacade summaryFacade;
    private static final String USER_ID = "66ea2da5-841c-4d16-9838-25cd2634af09";

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

        assertThatJson(result)
            .whenIgnoringPaths("date", "id")
            .isEqualTo(expected);

        assertThatJson(result)
            .inPath("id")
            .isNumber();

        String actualDate = JsonPath.read(result, "$.date");
        DateAssertion.assertDateFormat(actualDate, "dd.MM.yyyy HH:mm:ss");
    }

    @Test
    public void shouldUpdateSummary() throws Exception {
        // given
        String token = accessManagementFacade.login("user@gmail.com", "123").token();
        Summary newSummary = summaryFacade.createNewSummary(UUID.fromString(USER_ID));
        newSummary.addAsset(Asset.builder().name("new Asset").money(new Money(BigDecimal.ZERO)).buildAsset());

        // when
        String result = mockMvc.perform(
                MockMvcRequestBuilders.post("/" + USER_ID + "/summaries/" + newSummary.getId() + "/update")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType("application/json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(DTOMapper.dto(newSummary))))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        // then
        String expected = getFile("summaries/should_update_summary.json");

        assertThatJson(result)
            .whenIgnoringPaths("date", "id")
            .isEqualTo(expected);

        assertThatJson(result)
            .inPath("id")
            .isNumber();

        String actualDate = JsonPath.read(result, "$.date");
        DateAssertion.assertDateFormat(actualDate, "dd.MM.yyyy HH:mm:ss");
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

        assertThatJson(result)
            .whenIgnoringPaths("date", "id")
            .isEqualTo(expected);

        assertThatJson(result)
            .inPath("id")
            .isNumber();

        String actualDate = JsonPath.read(result, "$.date");
        DateAssertion.assertDateFormat(actualDate, "dd.MM.yyyy HH:mm:ss");
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
        assertThatJson(result)
            .whenIgnoringPaths("date", "id")
            .isEqualTo(expected);

        String actualDate = JsonPath.read(result, "$.date");
        DateAssertion.assertDateFormat(actualDate, "dd.MM.yyyy HH:mm:ss");
    }

}
