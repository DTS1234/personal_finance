import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import personal.finance.PersonalFinanceApplication;
import personal.finance.asset.Asset;
import personal.finance.asset.AssetRepository;
import personal.finance.asset.item.Item;
import personal.finance.asset.item.ItemDomain;
import personal.finance.asset.item.ItemRepository;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.PersistenceAdapter;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;

import javax.persistence.Persistence;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SummaryRepository.class, PersonalFinanceApplication.class})
class PersistenceAdapterTest {

    @LocalServerPort
    private int port;

    @Autowired
    private SummaryRepository summaryRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private ItemRepository itemRepository;
    PersistenceAdapter subject;

    @BeforeEach
    void setUp() {
        subject = new PersistenceAdapter(summaryRepository, assetRepository, itemRepository);
        summaryRepository.deleteAll();
        assetRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void shouldPersistAssetToSummary() {
        // given
        BigDecimal moneyValue = BigDecimal.valueOf(200.00).setScale(2, RoundingMode.HALF_UP);

        SummaryEntity summaryEntity = SummaryEntity.builder()
                .id(1L)
                .state(SummaryState.DRAFT).moneyValue(moneyValue.doubleValue())
                .date(LocalDate.of(2022, 1, 1))
                .build();
        summaryRepository.save(summaryEntity);

        Asset asset = Asset.builder()
                .name("asset1")
                .id(1L)
                .moneyValue(moneyValue)
                .summary(summaryEntity)
                .buildAsset();
        asset.addItem(Item.builder().name("account PLN").moneyValue(moneyValue).build());
        assetRepository.save(asset);


        // when
        subject.addAssetToSummary(
                AssetDomain.builder()
                        .id(2L)
                        .name("asset2")
                        .moneyValue(BigDecimal.valueOf(130).setScale(2, RoundingMode.HALF_UP))
                        .items(List.of(ItemDomain.builder().quantity(BigDecimal.valueOf(0.000123)).name("stock 1").moneyValue(moneyValue).build()))
                        .build(),
                SummaryDomain.builder().id(1L).build());

        // then
        SummaryEntity actual = summaryRepository.findById(1L).get();

        List<Asset> actualAssets = actual.getAssets();
        Assertions.assertThat(actualAssets.get(0)).usingRecursiveComparison().isEqualTo(Asset.builder()
                .name("asset1")
                .id(1L)
                .moneyValue(moneyValue)
                .summary(actual)
                 .items(List.of(Item.builder().id(actualAssets.get(0).getItems().get(0).getId()).name("account PLN").moneyValue(moneyValue).asset(actualAssets.get(0)).build()))
                .buildAsset());

        Assertions.assertThat(actualAssets.get(1)).usingRecursiveComparison().isEqualTo(Asset.builder()
                .id(2L)
                .name("asset2")
                .summary(actual)
                .moneyValue(BigDecimal.valueOf(130).setScale(2, RoundingMode.HALF_UP))
                .items(List.of(Item.builder().id(actualAssets.get(1).getItems().get(0).getId()).quantity(BigDecimal.valueOf(0.000123)).name("stock 1").moneyValue(moneyValue).asset(actualAssets.get(1)).build()))
                .buildAsset());
    }
}
