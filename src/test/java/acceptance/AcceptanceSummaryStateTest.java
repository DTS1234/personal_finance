package acceptance;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import personal.finance.PersonalFinanceApplication;
import personal.finance.asset.Asset;
import personal.finance.asset.AssetRepository;
import personal.finance.asset.item.Item;
import personal.finance.asset.item.ItemRepository;
import personal.finance.summary.SummaryState;
import personal.finance.summary.output.Summary;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {PersonalFinanceApplication.class}
)
public class AcceptanceSummaryStateTest {

    public static final String BASE_PATH = "http://localhost:%s";

    @LocalServerPort
    private int port;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        summaryRepository.deleteAll();
        assetRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        summaryRepository.deleteAll();
        assetRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void shouldCreateSummaryAddOneAssetAndMoveToConfirmed() {
        // create summary
        Summary build = Summary.builder().date(LocalDate.of(2022, 2, 1)).build();

        Summary actual = given()
                .contentType(ContentType.JSON)
                .body(build)
                .when()
                .post(String.format(BASE_PATH + "/summaries/new", port))
                .then()
                .extract()
                .as(new TypeRef<>() {
                });

        // add one asset
        Asset asset = GivenAssets.assets().get(0);

        List<Item> items = GivenItems.itemLists(GivenAssets.assets()).get(0);
        asset.addItems(items);
        Summary afterAdding = given()
                .contentType(ContentType.JSON)
                .body(asset)
                .when()
                .post(String.format(BASE_PATH + "/summaries/" + actual.getId() + "/add_asset", port))
                .then()
                .extract()
                .as(new TypeRef<>() {
                });

        Assertions.assertThat(afterAdding.getMoneyValue())
                        .isEqualTo(BigDecimal.valueOf(500.31));

        Assertions.assertThat(afterAdding.getAssets())
                .usingRecursiveComparison()
                .ignoringFields("id", "items")
                .isEqualTo(List.of(
                        Asset.builder()
                                .name("Crypto")
                                .moneyValue(BigDecimal.valueOf(500.31))
                                .items(items)
                                .buildAsset()
                ));

        Assertions.assertThat(afterAdding.getAssets().get(0).getItems())
                .usingRecursiveComparison()
                .ignoringFields("id", "asset")
                .isEqualTo(items);

        // confirm
        Summary confirmed = given()
                .contentType(ContentType.JSON)
                .body(afterAdding)
                .when()
                .post(String.format(BASE_PATH + "/summaries/" + actual.getId() + "/confirm", port))
                .then()
                .extract()
                .as(new TypeRef<>() {
                });

        Assertions.assertThat(confirmed)
                .isEqualTo(Summary.builder()
                        .state(SummaryState.CONFIRMED)
                        .id(confirmed.getId())
                        .assets(afterAdding.getAssets())
                        .moneyValue(asset.getMoneyValue())
                        .date(LocalDate.of(2022, 2, 1))
                        .build());
    }

    @Test
    void shouldEditAnAssetAddNewItemAndSave() {

        Item item = Item.builder()
                .quantity(BigDecimal.valueOf(500L))
                .moneyValue(BigDecimal.valueOf(200L))
                .name("Bitcoin")
                .build();

        Asset asset = Asset.builder()
                .name("Crypto")
                .moneyValue(BigDecimal.valueOf(1000L))
                .buildAsset();

        LocalDate date = LocalDate.now();
        SummaryEntity summary = SummaryEntity.builder()
                .moneyValue(BigDecimal.valueOf(1000L))
                .state(SummaryState.DRAFT)
                .date(date)
                .build();

        asset.addItem(item);
        summary.addAsset(asset);
        SummaryEntity savedInitialSummary = summaryRepository.save(summary);

        Summary newSummary = Summary.builder()
                .id(savedInitialSummary.getId())
                .state(SummaryState.DRAFT)
                .date(date)
                .assets(new ArrayList<>())
                .moneyValue(BigDecimal.valueOf(1500L))
                .build();

        Long assetId = savedInitialSummary.getAssets().get(0).getId();
        Asset assetEdited = Asset.builder()
                .id(assetId)
                .moneyValue(BigDecimal.valueOf(1500L))
                .name("Crypto")
                .buildAsset();

        Long itemId = savedInitialSummary.getAssets().get(0).getItems().get(0).getId();
        Item itemEdited = Item.builder()
                .id(itemId)
                .moneyValue(BigDecimal.valueOf(1500L))
                .quantity(BigDecimal.valueOf(501L))
                .name("Bitcoin")
                .build();

        assetEdited.addItem(itemEdited);;

        Summary edited = given()
                .contentType(ContentType.JSON)
                .body(assetEdited)
                .when()
                .post(String.format(BASE_PATH + "/summaries/" + savedInitialSummary.getId() + "/assets/" + assetId, port))
                .then()
                .extract()
                .as(new TypeRef<>() {
                });

        Assertions.assertThat(edited)
                .hasFieldOrPropertyWithValue("id", savedInitialSummary.getId())
                .hasFieldOrPropertyWithValue("state", SummaryState.DRAFT);
        Assertions.assertThat(edited.getAssets()).hasSize(1);
        Assertions.assertThat(edited.getAssets().get(0))
                .hasFieldOrPropertyWithValue("id", assetId)
                .hasFieldOrPropertyWithValue("moneyValue", BigDecimal.valueOf(1500L));
        Assertions.assertThat(edited.getAssets().get(0).getItems())
                .hasSize(1);
        Assertions.assertThat(edited.getAssets().get(0).getItems().get(0))
                .hasFieldOrPropertyWithValue("name", "Bitcoin")
                .hasFieldOrPropertyWithValue("quantity", BigDecimal.valueOf(501L))
                .hasFieldOrPropertyWithValue("moneyValue", BigDecimal.valueOf(1500L));
    }
}
