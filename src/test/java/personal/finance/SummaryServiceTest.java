package personal.finance;

import acceptance.GivenAssets;
import acceptance.GivenItems;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import personal.finance.asset.Asset;
import personal.finance.asset.AssetRepository;
import personal.finance.summary.SummaryService;
import personal.finance.summary.SummaryState;
import personal.finance.summary.output.Summary;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SummaryServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private SummaryRepository summaryRepository;

    private SummaryService subject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new SummaryService(assetRepository, summaryRepository);
    }

    @Test
    void givenSummaryInADraftAndNoSummariesAdded_shouldReturnAllAssetsAvailable() {
        // given
        Optional<SummaryEntity> summaryEntity = Optional.of(SummaryEntity.builder()
                .id(1L)
                .date(LocalDate.now())
                .moneyValue(BigDecimal.ZERO)
                .state(SummaryState.DRAFT)
                .build());
        when(summaryRepository.findById(1L)).thenReturn(
                summaryEntity);
        // when
        List<Asset> actual = subject.getAvailableAssets(1L);
        // then
        Assertions.assertThat(actual).isEqualTo(actual);
    }

    @Test
    void givenSummariesSaved_shouldReturnSummariesInConfirmedState() {
        when(summaryRepository.findSummaryByStateEqualsOrderById(SummaryState.CONFIRMED)).thenReturn(
                Arrays.asList(
                        SummaryEntity.builder().state(SummaryState.CONFIRMED).id(1L).build(),
                        SummaryEntity.builder().state(SummaryState.CONFIRMED).id(4L).build()
                )
        );

        List<Summary> actual = subject.getAllConfirmedSummaries();

        Assertions.assertThat(actual)
                .containsAll(Arrays.asList(
                        Summary.builder().id(1L).state(SummaryState.CONFIRMED).build(),
                        Summary.builder().id(4L).state(SummaryState.CONFIRMED).build()));
    }

    @Test
    void shouldCreateNewSummaryWithInitialValues() {
        Summary requestSummary = Summary.builder().date(LocalDate.of(2022, 2, 1)).build();

        Mockito.when(summaryRepository.save(any()))
                .thenReturn(SummaryEntity.builder()
                        .id(1L)
                        .date(requestSummary.getDate())
                        .state(SummaryState.DRAFT)
                        .moneyValue(BigDecimal.ZERO)
                        .assets(Collections.emptyList())
                        .build());

        Summary actual = subject.createNewSummary(requestSummary);

        Assertions.assertThat(actual.getId()).isNotNull();
        Assertions.assertThat(actual.getMoneyValue()).isEqualByComparingTo(BigDecimal.ZERO);
        Assertions.assertThat(actual.getState()).isEqualTo(SummaryState.DRAFT);
        Assertions.assertThat(actual.getDate()).isEqualTo(LocalDate.of(2022, 2, 1));
        Assertions.assertThat(actual.getAssets()).isEmpty();
    }

    @Test
    void shouldAddAssetInDraftState() {
        SummaryEntity summaryEntity = SummaryEntity.builder()
                .id(1L)
                .date(LocalDate.now())
                .moneyValue(BigDecimal.ZERO)
                .state(SummaryState.DRAFT)
                .assets(new ArrayList<>())
                .build();

        Asset newAsset = GivenAssets.assets().get(0);
        newAsset.addItems(GivenItems.itemLists(GivenAssets.assets()).get(0));

        when(summaryRepository.findById(1L)).thenReturn(
                Optional.ofNullable(summaryEntity),
                Optional.ofNullable(summaryEntity)
        );

        when(summaryRepository.save(any())).thenReturn(SummaryEntity.builder()
                .id(1L)
                .date(LocalDate.now())
                .moneyValue(BigDecimal.ZERO)
                .state(SummaryState.DRAFT)
                .assets(new ArrayList<>(List.of(newAsset)))
                .build());

        Summary afterAdding = subject.addAsset(1L, newAsset);

        Assertions.assertThat(afterAdding)
                .isEqualTo(Summary.builder()
                        .id(1L)
                        .date(LocalDate.now())
                        .moneyValue(BigDecimal.ZERO)
                        .state(SummaryState.DRAFT)
                        .assets(Collections.singletonList(newAsset))
                        .build());
    }

    @Test
    void shouldThrowAnExceptionIfSummaryToBeCreatedHasNoAssets() {
        when(summaryRepository.findById(1L))
                .thenReturn(Optional.of(SummaryEntity.builder()
                        .date(LocalDate.now())
                        .moneyValue(BigDecimal.ZERO)
                        .assets(Collections.emptyList())
                        .id(1L)
                        .state(SummaryState.DRAFT)
                        .build()));

        Assertions.assertThatThrownBy(() -> subject.confirmSummary(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("You cannot confirm empty summary.");
    }

    @Test
    void shouldThrowAnExceptionIfSummaryNotInDraft() {
        when(summaryRepository.findById(1L))
                .thenReturn(Optional.of(SummaryEntity.builder()
                        .date(LocalDate.now())
                        .moneyValue(BigDecimal.ZERO)
                        .assets(List.of(Asset.builder().buildAsset()))
                        .id(1L)
                        .state(SummaryState.CANCELED)
                        .build()));

        Assertions.assertThatThrownBy(() -> subject.confirmSummary(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("You cannot confirm summary in that state: " + SummaryState.CANCELED);
    }

    @Test
    void shouldThrowAnExceptionIfSummaryMoneyValueIsDifferentToAssetMoneyValue() {
        when(summaryRepository.findById(1L))
                .thenReturn(Optional.of(SummaryEntity.builder()
                        .date(LocalDate.now())
                        .moneyValue(BigDecimal.valueOf(199L))
                        .assets(List.of(Asset.builder().moneyValue(BigDecimal.valueOf(200L).setScale(2, RoundingMode.HALF_UP)).buildAsset()))
                        .id(1L)
                        .state(SummaryState.DRAFT)
                        .build()));

        Assertions.assertThatThrownBy(() -> subject.confirmSummary(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("Money value for summary should be equal to sum of money value of all assets. %s != %s", 199.00, 200.00));
    }

    @Test
    void shouldConfirmSummaryAndChangeStateToConfirmed() {
        when(summaryRepository.findById(1L))
                .thenReturn(Optional.of(SummaryEntity.builder()
                        .date(LocalDate.now())
                        .moneyValue(BigDecimal.valueOf(200L))
                        .assets(List.of(Asset.builder().moneyValue(BigDecimal.valueOf(200L).setScale(2, RoundingMode.HALF_UP)).buildAsset()))
                        .id(1L)
                        .state(SummaryState.DRAFT)
                        .build()));

        when(summaryRepository.save(any()))
                .thenReturn(SummaryEntity.builder()
                        .date(LocalDate.now())
                        .moneyValue(BigDecimal.valueOf(200L))
                        .assets(List.of(Asset.builder().moneyValue(BigDecimal.valueOf(200L).setScale(2, RoundingMode.HALF_UP)).buildAsset()))
                        .id(1L)
                        .state(SummaryState.CONFIRMED)
                        .build());

        Summary actual = subject.confirmSummary(1L);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(Summary.builder()
                        .date(LocalDate.now())
                        .moneyValue(BigDecimal.valueOf(200L))
                        .assets(List.of(Asset.builder().moneyValue(BigDecimal.valueOf(200L).setScale(2, RoundingMode.HALF_UP)).buildAsset()))
                        .id(1L)
                        .state(SummaryState.CONFIRMED)
                        .build());
    }


}
