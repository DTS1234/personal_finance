package acceptance;

import io.restassured.common.mapper.TypeRef;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import personal.finance.*;
import org.junit.jupiter.api.Test;

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
                Item.builder().id(1L).asset(asset1).build(),
                Item.builder().id(2L).asset(asset1).build(),
                Item.builder().id(3L).asset(asset1).build()));

        Asset asset2 = assetRepository.findById(2L).get();
        itemRepository.save(Item.builder().id(4L).asset(asset2).build());

        Asset asset3 = assetRepository.findById(3L).get();
        itemRepository.saveAll(Arrays.asList(
                Item.builder().id(5L).asset(asset3).build(),
                Item.builder().id(6L).asset(asset3).build()));

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
                .hasFieldOrPropertyWithValue("moneyValue", 500.31)
                .hasFieldOrPropertyWithValue("items", Arrays.asList(
                        Item.builder().id(1L).build(),
                        Item.builder().id(2L).build(),
                        Item.builder().id(3L).build()));

        Asset secondAsset = actual.get(1);
        Assertions.assertThat(secondAsset)
                .hasFieldOrPropertyWithValue("name", "Account 1")
                .hasFieldOrPropertyWithValue("moneyValue", 1500.12)
                .hasFieldOrPropertyWithValue("items", Collections.singletonList(
                        Item.builder().id(4L).build()));

        Asset thirdAsset = actual.get(2);
        Assertions.assertThat(thirdAsset)
                .hasFieldOrPropertyWithValue("name", "Stocks 1")
                .hasFieldOrPropertyWithValue("moneyValue", 2201.24)
                .hasFieldOrPropertyWithValue("items", Arrays.asList(
                        Item.builder().id(5L).build(),
                        Item.builder().id(6L).build()));
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
