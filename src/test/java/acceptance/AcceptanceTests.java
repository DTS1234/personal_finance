package acceptance;

import io.restassured.common.mapper.TypeRef;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import personal.finance.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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

    @LocalServerPort
    private int port;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void givenThreeAssetsSaved_listOfThreeAssetsShouldBeReturned() {
        // given
        assetRepository.saveAll(GivenAssets.assets());

        Asset asset1 = assetRepository.findById(1L).get();
        itemRepository.saveAll(Arrays.asList(
                Item.builder().id(1L).moneyValue(BigDecimal.valueOf(500.21)).quantity(BigDecimal.valueOf(0.0000123)).name("Bitcoin").asset(asset1).build(),
                Item.builder().id(2L).moneyValue(BigDecimal.valueOf(1000.22)).quantity(BigDecimal.valueOf(0.00543)).name("Ethereum").asset(asset1).build(),
                Item.builder().id(3L).moneyValue(BigDecimal.valueOf(500.01)).quantity(BigDecimal.valueOf(100.00)).name("Luna").asset(asset1).build()));

        Asset asset2 = assetRepository.findById(2L).get();
        itemRepository.save(Item.builder().moneyValue(BigDecimal.valueOf(2201.24)).id(4L).quantity(BigDecimal.valueOf(1)).name("ZL account").asset(asset2).build());

        Asset asset3 = assetRepository.findById(3L).get();
        itemRepository.saveAll(Arrays.asList(
                Item.builder().id(5L).moneyValue(BigDecimal.valueOf(1201.12)).quantity(BigDecimal.valueOf(12)).name("CDP").asset(asset3).build(),
                Item.builder().id(6L).moneyValue(BigDecimal.valueOf(1000.12)).quantity(BigDecimal.valueOf(43)).name("ALG").asset(asset3).build()));

        // when
        List<Asset> actual = when()
                .get(String.format("http://localhost:%s/assets", port))
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
                .get(String.format("http://localhost:%s/assets/sum", port))
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
                .get(String.format("http://localhost:%s/assets/percentages", port))
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
    void givenFourSummaries_listOfFourSummariesShouldBeReturned() {
        // given
        summaryRepository.saveAll(
                Arrays.asList(Summary.builder().id(1L).moneyValue(500.31).date(LocalDate.of(2022, 1, 1)).build(),
                        Summary.builder().id(2L).moneyValue(1500.12).date(LocalDate.of(2022, 2, 1)).build(),
                        Summary.builder().id(3L).moneyValue(2201.24).date(LocalDate.of(2022, 3, 1)).build(),
                        Summary.builder().id(4L).moneyValue(3127.59).date(LocalDate.of(2022, 4, 1)).build())
        );
        // when
        List<Summary> actual = when()
                .get(String.format("http://localhost:%s/summaries", port))
                .then()
                .extract()
                .as(new TypeRef<>() {
                });
        // then
        Assertions.assertThat(actual)
                .hasSize(4);

        Summary firstSummary = actual.get(0);
        Assertions.assertThat(firstSummary)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("moneyValue", 500.31)
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 1, 1));

        Summary secondSummary = actual.get(1);
        Assertions.assertThat(secondSummary)
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("moneyValue", 1500.12)
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 2, 1));

        Summary thirdSummary = actual.get(2);
        Assertions.assertThat(thirdSummary)
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("moneyValue", 2201.24)
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 3, 1));

        Summary fourthSummary = actual.get(3);
        Assertions.assertThat(fourthSummary)
                .hasFieldOrPropertyWithValue("id", 4L)
                .hasFieldOrPropertyWithValue("moneyValue", 3127.59)
                .hasFieldOrPropertyWithValue("date", LocalDate.of(2022, 4, 1));

    }
}
