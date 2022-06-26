package personal.finance;

import acceptance.GivenAssets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import personal.finance.asset.Asset;
import personal.finance.asset.AssetRepository;
import personal.finance.summary.SummaryState;
import personal.finance.summary.output.Summary;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;
import personal.finance.summary.SummaryService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        List<Asset> assets = GivenAssets.assets();
        assetRepository.saveAll(assets);

        Optional<SummaryEntity> summaryEntity = Optional.of(SummaryEntity.builder()
                .id(1L)
                .date(LocalDate.now())
                .moneyValue(0L)
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
}
