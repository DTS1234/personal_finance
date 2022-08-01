package personal.finance.summary.domain.action;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import personal.finance.asset.AssetMapper;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.PersistenceAdapter;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;
import personal.finance.summary.persistance.SummaryEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.when;

class AddAssetDraftActionTest {

    @Mock
    private PersistenceAdapter persistenceAdapter;

    private AddAssetDraftAction subject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new AddAssetDraftAction(persistenceAdapter);
    }

    @Test
    void givenSummaryNotInDraft_shouldThrowAnException() {
        Assertions.assertThatThrownBy(() -> subject.execute(AssetDomain.builder().build(), SummaryDomain.builder().state(SummaryState.CONFIRMED).build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("You cannot add that asset in %s state.", SummaryState.CONFIRMED));
    }

    @Test
    void givenSummaryAlreadyContainsAssetWithThatName_shouldThrowAnException() {

        AssetDomain assetDomain = AssetDomain.builder().name("name1").build();
        SummaryDomain summary = SummaryDomain.builder().state(SummaryState.DRAFT).assets(Collections.singletonList(assetDomain)).build();

        Assertions.assertThatThrownBy(() -> subject.execute(assetDomain, summary))
                .isInstanceOf(AssetWithThatNameAlreadyExistsException.class);
    }

    @Test
    void givenNewValidAssetPassed_shouldAddItToSummaryAndPersist() {
        AssetDomain asset1 = AssetDomain.builder().name("name1").items(Collections.emptyList()).moneyValue(BigDecimal.ZERO).build();
        AssetDomain asset2 = AssetDomain.builder().name("name2").items(Collections.emptyList()).moneyValue(BigDecimal.valueOf(11.12)).build();
        SummaryDomain summary = SummaryDomain.builder().state(SummaryState.DRAFT).assets(new ArrayList<>(Collections.singleton(asset1))).build();

        Mockito.when(persistenceAdapter.addAssetToSummary(asset2, summary)).thenReturn(SummaryEntity.builder()
                        .id(1L)
                        .date(LocalDate.now())
                        .state(SummaryState.DRAFT)
                        .assets(Stream.of(asset1, asset2).map(AssetMapper::toEntity).collect(Collectors.toList()))
                        .moneyValue(BigDecimal.valueOf(11.12))
                .build());

        SummaryDomain actual = subject.execute(asset2, summary);

        Assertions.assertThat(summary.getAssets()).containsExactlyInAnyOrder(asset1, asset2);
        Assertions.assertThat(summary.getMoneyValue()).isEqualTo(BigDecimal.valueOf(11.12));

        Assertions.assertThat(actual.getAssets()).containsExactlyInAnyOrder(asset1, asset2);
        Assertions.assertThat(actual.getMoneyValue()).isEqualTo(BigDecimal.valueOf(11.12));
    }

    @Test
    void shouldAlignSummaryMoneyValueWithAssetSumMoneyValue() {
        AssetDomain asset1 = AssetDomain.builder().name("name1").items(Collections.emptyList()).moneyValue(BigDecimal.TEN).build();
        AssetDomain asset2 = AssetDomain.builder().name("name2").items(Collections.emptyList()).moneyValue(BigDecimal.TEN).build();
        SummaryDomain summary = SummaryDomain.builder().state(SummaryState.DRAFT).moneyValue(BigDecimal.TEN).assets(new ArrayList<>(Collections.singleton(asset1))).build();

        Mockito.when(persistenceAdapter.addAssetToSummary(asset2, summary)).thenReturn(SummaryEntity.builder()
                .id(1L)
                .date(LocalDate.now())
                .state(SummaryState.DRAFT)
                .assets(Stream.of(asset1, asset2).map(AssetMapper::toEntity).collect(Collectors.toList()))
                .moneyValue(BigDecimal.TEN.add(BigDecimal.TEN))
                .build());

        SummaryDomain actual = subject.execute(asset2, summary);

        Assertions.assertThat(summary.getMoneyValue()).isEqualTo(BigDecimal.TEN.add(BigDecimal.TEN));
        Assertions.assertThat(summary.getAssets()).containsExactlyInAnyOrder(asset1, asset2);
        Assertions.assertThat(actual.getAssets()).containsExactlyInAnyOrder(asset1, asset2);
        Assertions.assertThat(actual.getMoneyValue()).isEqualTo(BigDecimal.TEN.add(BigDecimal.TEN));
    }

}
