package integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import personal.finance.iam.application.AccessManagementFacade;
import personal.finance.iam.application.dto.AuthResponseDTO;
import personal.finance.tracking.asset.application.AssetDTOMapper;
import personal.finance.tracking.asset.application.AssetFacade;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetType;
import personal.finance.tracking.asset.domain.Item;
import personal.finance.tracking.asset.domain.ItemId;
import personal.finance.tracking.summary.application.SummaryFacade;
import personal.finance.tracking.summary.application.dto.DTOMapper;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static integration.Fixtures.USER_ID;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AssetIntegrationTest extends IntegrationTest {

    @Autowired
    private AccessManagementFacade accessManagementFacade;

    @Autowired
    private SummaryFacade summaryFacade;

    @Autowired
    private AssetFacade assetFacade;

    @Test
    void should_update_asset_in_summary() throws Exception {
        AuthResponseDTO login = accessManagementFacade.login("user@gmail.com", "123");
        String token = login.token();

        Summary newSummary = summaryFacade.createNewSummary(UUID.fromString(login.id()));
        Item oldItem = Item.builder().id(ItemId.random()).name("Item 1").money(new Money(5)).quantity(BigDecimal.ONE)
            .build();
        Asset oldAsset = Asset.builder()
            .id(AssetId.random())
            .name("Asset")
            .type(AssetType.NORMAL)
            .money(new Money(5))
            .items(List.of(oldItem))
            .buildAsset();

        newSummary.addAsset(oldAsset);
        Summary summary = summaryFacade.updateSummaryInDraft(DTOMapper.dto(newSummary), UUID.fromString(login.id()));

        String summaryId = summary.getIdValue().toString();
        String assetId = summary.getAssets().get(0).getIdValue().toString();

        Asset newAsset = Asset.builder()
            .id(new AssetId(UUID.fromString(assetId)))
            .type(AssetType.NORMAL)
            .name("Asset 1")
            .money(new Money(10))
            .items(List.of(
                Item.builder().name("Item 1").money(new Money(10)).quantity(BigDecimal.ONE)
                    .build()))
            .summaryId(summary.getId())
            .buildAsset();

        String result = mockMvc.perform(
                MockMvcRequestBuilders.post("/" + USER_ID + "/summaries/" + summaryId + "/asset/" + assetId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType("application/json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(AssetDTOMapper.dto(newAsset))))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        // then
        String expected = getFile("asset/should_update_asset.json");

        assertThatJson(result)
            .whenIgnoringPaths("date", "id", "items[0].id", "summaryId")
            .isEqualTo(expected);

        String queryResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/summaries/" + summaryId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType("application/json")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();



    }

    @Test
    void should_add_a_new_asset_to_a_summary() throws Exception {
        AuthResponseDTO login = accessManagementFacade.login("user@empty.pl", "123");
        String token = login.token();

        Summary newSummary = summaryFacade.createNewSummary(UUID.fromString(login.id()));
        Item item = Item.builder().id(ItemId.random()).name("Item 1").money(new Money(5)).quantity(BigDecimal.ONE)
            .build();
        Asset newAsset = Asset.builder()
            .id(AssetId.random())
            .name("Asset 1")
            .type(AssetType.NORMAL)
            .money(new Money(5))
            .items(List.of(item))
            .summaryId(newSummary.getId())
            .buildAsset();

        String result = mockMvc.perform(
                MockMvcRequestBuilders.post("/" + USER_ID + "/summaries/" + newSummary.getIdValue() + "/asset/add")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType("application/json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(AssetDTOMapper.dto(newAsset))))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        // then
        String expected = getFile("asset/should_add_asset.json");

        assertThatJson(result)
            .whenIgnoringPaths("date", "id", "items[0].id", "summaryId")
            .isEqualTo(expected);
        Thread.sleep(2000);
        String queryResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/summaries/" + newSummary.getIdValue())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType("application/json")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        String expectedQueryResult = getFile("asset/should_return_summary_with_added_asset.json");
        assertThatJson(queryResult)
            .whenIgnoringPaths("date", "id", "assets[0].id", "userId", "assets[0].items[0].id", "assets[0].summaryId")
            .isEqualTo(expectedQueryResult);
    }
}
