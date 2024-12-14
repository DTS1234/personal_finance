package integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.iam.application.AccessManagementFacade;
import personal.finance.iam.application.dto.AuthResponseDTO;
import personal.finance.tracking.asset.application.AssetDTOMapper;
import personal.finance.tracking.asset.application.AssetFacade;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetType;
import personal.finance.tracking.asset.domain.CustomItem;
import personal.finance.tracking.asset.domain.ItemId;
import personal.finance.tracking.asset.domain.StockItem;
import personal.finance.tracking.summary.application.SummaryFacade;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryId;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static personal.finance.Fixtures.USER_ID;

@Transactional
public class AssetIntegrationTest extends IntegrationTest {

    @Autowired
    private AccessManagementFacade accessManagementFacade;

    @Autowired
    private SummaryFacade summaryFacade;

    @Autowired
    private AssetFacade assetFacade;

    @Test
    void should_update_asset_in_summary() throws Exception {
        AuthResponseDTO login = accessManagementFacade.login("user@empty.pl", "123");
        String token = login.token();

        Summary newSummary = summaryFacade.createNewSummary(UUID.fromString(login.id()));
        CustomItem oldItem = CustomItem.builder().id(ItemId.random()).name("Item 1").money(new Money(5)).build();
        Asset oldAsset = Asset.builder()
            .id(AssetId.random())
            .name("Asset")
            .type(AssetType.CUSTOM)
            .money(new Money(5))
            .items(List.of(oldItem))
            .summaryId(newSummary.getId())
            .buildAsset();

        assetFacade.createAsset(newSummary.getIdValue(), oldAsset);

        UUID summaryId = newSummary.getIdValue();
        UUID assetId = oldAsset.getIdValue();

        Asset newAsset = Asset.builder()
            .id(new AssetId(assetId))
            .type(AssetType.CUSTOM)
            .name("Asset 1")
            .money(new Money(10))
            .items(List.of(
                CustomItem.builder().id(ItemId.random()).name("Item 1").money(new Money(10))
                    .build()))
            .summaryId(new SummaryId(summaryId))
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

        String expectedQueryResult = getFile("asset/should_return_updated_summary.json");
        assertThatJson(queryResult)
            .whenIgnoringPaths("date", "id", "assets[0].id", "userId", "assets[0].items[0].id", "assets[0].summaryId")
            .isEqualTo(expectedQueryResult);
    }

    @Test
    void should_add_a_new_asset_to_a_summary() throws Exception {
        AuthResponseDTO login = accessManagementFacade.login("user@empty.pl", "123");
        String token = login.token();

        Summary newSummary = summaryFacade.createNewSummary(UUID.fromString(login.id()));
        CustomItem item = CustomItem.builder().id(ItemId.random()).name("Item 1").money(new Money(5)).build();
        Asset newAsset = Asset.builder()
            .id(AssetId.random())
            .name("Asset 1")
            .type(AssetType.CUSTOM)
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

    @Test
    void should_add_a_new_stock_asset_to_a_summary() throws Exception {
        AuthResponseDTO login = accessManagementFacade.login("user@empty.pl", "123");
        String token = login.token();

        Summary newSummary = summaryFacade.createNewSummary(UUID.fromString(login.id()));
        StockItem item = StockItem.builder()
            .id(ItemId.random())
            .name("Item 1")
            .money(new Money(5))
            .purchasePrice(new Money(3))
            .currentPrice(new Money(5))
            .quantity(BigDecimal.ONE)
            .ticker("Stock Item")
            .build();
        Asset newAsset = Asset.builder()
            .id(AssetId.random())
            .name("Asset 1")
            .type(AssetType.STOCK)
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
        String expected = getFile("asset/should_add_stock_asset.json");

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

        String expectedQueryResult = getFile("asset/should_return_summary_with_added_stock_asset.json");
        assertThatJson(queryResult)
            .whenIgnoringPaths("date", "id", "assets[0].id", "userId", "assets[0].items[0].id", "assets[0].summaryId")
            .isEqualTo(expectedQueryResult);
    }
}
