package acceptance;

import io.restassured.common.mapper.TypeRef;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import personal.finance.*;
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
import java.util.*;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.when;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {AssetRepository.class, PersonalFinanceApplication.class}
)
class AcceptanceTests {

    public static final String BASE_PATH = "http://localhost:%s";
    @LocalServerPort
    private int port;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @AfterEach
    void tearDown() {
        summaryRepository.deleteAll();
        assetRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void givenThreeAssetsSaved_listOfThreeAssetsShouldBeReturned() {
        // given
        List<Asset> givenAssets = GivenAssets.assets();
        List<List<Item>> givenItems = GivenItems.itemLists(givenAssets);
        IntStream.range(0, 3).forEach(i -> givenAssets.get(i).addItems(givenItems.get(i)));
        assetRepository.saveAll(givenAssets);
        // when
        List<Asset> actual = when()
                .get(String.format(BASE_PATH + "/assets", port))
                .then()
                .extract()
                .as(new TypeRef<>() {
                });

        // then
        Assertions.assertThat(actual)
                .hasSize(3);

        Asset firstAsset = actual.get(0);
        Assertions.assertThat(firstAsset)
                .hasFieldOrPropertyWithValue("name", "Crypto")
                .hasFieldOrPropertyWithValue("moneyValue", BigDecimal.valueOf(500.31));
        List<Item> expectedItems1 = Arrays.asList(
                Item.builder().id(1L).moneyValue(BigDecimal.valueOf(500.21)).quantity(BigDecimal.valueOf(0.0000123)).name("Bitcoin").build(),
                Item.builder().id(2L).moneyValue(BigDecimal.valueOf(1000.22)).quantity(BigDecimal.valueOf(0.00543)).name("Ethereum").build(),
                Item.builder().id(3L).moneyValue(BigDecimal.valueOf(500.01)).quantity(BigDecimal.valueOf(100.00)).name("Luna").build());
        List<Item> actualItems1 = firstAsset.getItems();
        checkItemCorrectness(expectedItems1, actualItems1);

        Asset secondAsset = actual.get(1);
        Assertions.assertThat(secondAsset)
                .hasFieldOrPropertyWithValue("name", "Account 1")
                .hasFieldOrPropertyWithValue("moneyValue", BigDecimal.valueOf(1500.12));

        List<Item> expectedItems2 = Collections.singletonList(Item.builder().moneyValue(BigDecimal.valueOf(2201.24)).name("ZL account").quantity(BigDecimal.valueOf(1)).id(4L).build());
        List<Item> actualItems2 = secondAsset.getItems();
        checkItemCorrectness(expectedItems2, actualItems2);

        Asset thirdAsset = actual.get(2);
        Assertions.assertThat(thirdAsset)
                .hasFieldOrPropertyWithValue("name", "Stocks 1")
                .hasFieldOrPropertyWithValue("moneyValue", BigDecimal.valueOf(2201.24));
        List<Item> expectedItems3 = Arrays.asList(
                Item.builder().moneyValue(BigDecimal.valueOf(1201.12)).id(5L).name("CDP").quantity(BigDecimal.valueOf(12)).build(),
                Item.builder().moneyValue(BigDecimal.valueOf(1000.12)).id(6L).name("ALG").quantity(BigDecimal.valueOf(43)).build());
        List<Item> actualItems3 = thirdAsset.getItems();
        checkItemCorrectness(expectedItems3, actualItems3);
    }

    private void checkItemCorrectness(List<Item> expectedItems1, List<Item> actualItems1) {
        for (int i = 0; i < expectedItems1.size(); i++) {
            Item currExpectedItem = expectedItems1.get(i);
            Item currActualItem = actualItems1.get(i);
            Assertions.assertThat(currActualItem).usingRecursiveComparison().ignoringFields("moneyValue", "asset").isEqualTo(currExpectedItem);
            Assertions.assertThat(currActualItem.getMoneyValue()).isEqualByComparingTo(currExpectedItem.getMoneyValue());
        }
    }

    @Test
    void givenThreeAssetsWithMoneyValue_shouldReturnTheSumOfValue() {
        // given
        assetRepository.saveAll(GivenAssets.assets());
        // when
        double actual = when()
                .get(String.format(BASE_PATH + "/assets/sum", port))
                .then()
                .extract()
                .as(new TypeRef<>() {
                });
        // then
        Assertions.assertThat(actual).isEqualTo(4201.67);
    }

    @Test
    void givenThreeAssetsWithMoneyValue_shouldReturnThePercentagesMap() {
        // given
        List<Asset> given = GivenAssets.assets();
        assetRepository.saveAll(given);
        // when
        HashMap<Long, Double> actual = when()
                .get(String.format(BASE_PATH + "/assets/percentages", port))
                .then()
                .extract()
                .as(new TypeRef<>() {
                });
        // then
        Map<Long, Double> expected = new HashMap<>();
        expected.put(given.get(0).getId(), 11.91);
        expected.put(given.get(1).getId(), 35.70);
        expected.put(given.get(2).getId(), 52.39);
        Assertions.assertThat(actual).hasSize(3).containsExactlyEntriesOf(
                expected
        );
        Assertions.assertThat(expected.values().stream().reduce(0.0, Double::sum)).isEqualTo(100.00);
    }

    @Test
    void givenFiveSummaries_listOfThreeSummariesWithStateConfirmedShouldBeReturned() {
        // given
        summaryRepository.saveAll(
                Arrays.asList(SummaryEntity.builder().id(1L).moneyValue(500.31).date(LocalDate.of(2022, 1, 1)).state(SummaryState.CONFIRMED).build(),
                        SummaryEntity.builder().id(2L).moneyValue(1500.12).date(LocalDate.of(2022, 2, 1)).state(SummaryState.CANCELED).build(),
                        SummaryEntity.builder().id(3L).moneyValue(2201.24).date(LocalDate.of(2022, 3, 1)).state(SummaryState.CONFIRMED).build(),
                        SummaryEntity.builder().id(4L).moneyValue(3127.59).date(LocalDate.of(2022, 4, 1)).state(SummaryState.CONFIRMED).build(),
                        SummaryEntity.builder().id(5L).moneyValue(1000.12).date(LocalDate.of(2022, 5, 1)).state(SummaryState.DRAFT).build()
                ));
        // when
        List<Summary> actual = when()
                .get(String.format(BASE_PATH + "/summaries", port))
                .then()
                .extract()
                .as(new TypeRef<>() {
                });
        // then
        Assertions.assertThat(actual)
                .hasSize(3);

        Summary firstSummary = actual.get(0);
        Assertions.assertThat(firstSummary)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("moneyValue", 500.31)
                .hasFieldOrPropertyWithValue("state", SummaryState.CONFIRMED)
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 1, 1));

        Summary thirdSummary = actual.get(1);
        Assertions.assertThat(thirdSummary)
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("moneyValue", 2201.24)
                .hasFieldOrPropertyWithValue("state", SummaryState.CONFIRMED)
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 3, 1));

        Summary fourthSummary = actual.get(2);
        Assertions.assertThat(fourthSummary)
                .hasFieldOrPropertyWithValue("id", 4L)
                .hasFieldOrPropertyWithValue("moneyValue", 3127.59)
                .hasFieldOrPropertyWithValue("state", SummaryState.CONFIRMED)
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 4, 1));

    }

    @Test
    void givenBrandNewSummary_ShouldShowAllAssetsAsAvailableForSummaryCreation() {

        assetRepository.saveAll(GivenAssets.assets());

        List<Asset> actual = when()
                .get(String.format(BASE_PATH + "/summaries/1/available_assets", port))
                .then()
                .extract()
                .as(new TypeRef<>() {
                });

        Assertions.assertThat(actual).isEqualTo(Arrays.asList(
                Asset.builder()
                        .id(1L)
                        .name("Crypto")
                        .moneyValue(BigDecimal.valueOf(500.31))
                        .items(Collections.emptyList())
                        .buildAsset(),
                Asset.builder()
                        .id(2L)
                        .name("Account 1")
                        .moneyValue(BigDecimal.valueOf(1500.12))
                        .items(Collections.emptyList())
                        .buildAsset(),
                Asset.builder()
                        .id(3L)
                        .name("Stocks 1")
                        .moneyValue(BigDecimal.valueOf(2201.24))
                        .items(Collections.emptyList())
                        .buildAsset()
        ));

    }

}
