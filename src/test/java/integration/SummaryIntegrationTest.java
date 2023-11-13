package integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import personal.finance.iam.application.AccessManagementFacade;
import personal.finance.summary.application.SummaryFacade;
import personal.finance.summary.application.dto.DTOMapper;
import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;

import java.math.BigDecimal;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SummaryIntegrationTest extends IntegrationTest {

    @Autowired
    private AccessManagementFacade accessManagementFacade;
    @Autowired
    private SummaryFacade summaryFacade;

    @Test
    public void shouldCreateSummary() throws Exception {
        // given
        String token = accessManagementFacade.login("user@gmail.com", "123").token();
        // when
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/1/summaries/new")
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

        assertThatJson(expected)
            .inPath("id")
            .isNumber();
    }

    @Test
    public void shouldUpdateSummary() throws Exception {
        // given
        String token = accessManagementFacade.login("user@gmail.com", "123").token();
        Summary newSummary = summaryFacade.createNewSummary(1L);
        newSummary.addAsset(Asset.builder().name("new Asset").money(new Money(BigDecimal.ZERO)).buildAsset());

        // when
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/1/summaries/" + newSummary.getId() + "/update")
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

        assertThatJson(expected)
            .inPath("id")
            .isNumber();
    }

    @Test
    public void shouldConfirmSummary() throws Exception {
        // given
        String token = accessManagementFacade.login("user@gmail.com", "123").token();
        Summary newSummary = summaryFacade.createNewSummary(1L);

        // when
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/1/summaries/" + newSummary.getId() + "/confirm")
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

        assertThatJson(expected)
            .inPath("id")
            .isNumber();
    }
}
