package personal.finance.summary.domain.action;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import personal.finance.asset.item.Item;
import personal.finance.asset.item.ItemDomain;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.PersistenceAdapter;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class ChangeAssetDraftActionTest {

    @Mock
    private PersistenceAdapter persistenceAdapter;

    private ChangeAssetDraftAction subject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new ChangeAssetDraftAction(persistenceAdapter);
    }

    @Test
    void shouldThrowAnExceptionIfNotInDraftState() {
        Assertions.assertThatThrownBy(() -> subject.execute(
                        AssetDomain.builder().build(),
                        SummaryDomain.builder().state(SummaryState.CANCELED).build()
                ))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("Cannot edit summary in that state: %s.", SummaryState.CANCELED));
    }

    @Test
    void shouldThrowAnExceptionIfAssetDoesNotExist() {
        String message = String.format("Asset with id of %s, does not exist.", 99L);
        Assertions.assertThatThrownBy(() -> subject.execute(
                        AssetDomain.builder().id(99L).build(),
                        SummaryDomain.builder().state(SummaryState.DRAFT).build()
                ))
                .isInstanceOf(AssetDoesNotExistException.class)
                .hasMessage(message);
    }

    @Test
    void shouldThrowExceptionIfNewMoneyValueIsNegative() {
        Mockito.when(persistenceAdapter.exists(any())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> subject.execute(
                        AssetDomain.builder().id(1L).moneyValue(BigDecimal.valueOf(-200)).build(),
                        SummaryDomain.builder().state(SummaryState.DRAFT).build()
                ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Money value cannot be negative!");
    }

    @Test
    void shouldPersistChangesIfNewAssetIsValid() {
        Mockito.when(persistenceAdapter.exists(any())).thenReturn(true);
        BigDecimal newMoneyValue = BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP);
        BigDecimal oldMoneyValue = BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP);
        subject.execute(AssetDomain.builder()
                        .id(1L)
                        .moneyValue(newMoneyValue)
                        .items(List.of(ItemDomain.builder()
                                .moneyValue(newMoneyValue)
                                .quantity(BigDecimal.valueOf(2))
                                .name("BTC")
                                .build()))
                        .name("Crypto").build(),
                SummaryDomain.builder()
                        .state(SummaryState.DRAFT)
                        .id(1L)
                        .localDate(LocalDate.of(2022, 1, 1))
                        .assets(Collections.singletonList(
                                AssetDomain.builder().id(1L).moneyValue(newMoneyValue).name("Crypto")
                                        .items(List.of(ItemDomain.builder().moneyValue(oldMoneyValue).quantity(BigDecimal.valueOf(2)).name("Bitcoin").build()))
                                        .build()
                        ))
                        .moneyValue(oldMoneyValue)
                        .build());
    }

}
