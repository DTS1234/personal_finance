package personal.finance.tracking.summary.application;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.SummaryRepository;
import personal.finance.tracking.summary.domain.SummaryState;
import personal.finance.tracking.summary.infrastracture.persistance.repository.SummaryInMemoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static personal.finance.tracking.asset.domain.AssetType.CUSTOM;

@DisplayNameGeneration(ReplaceUnderscores.class)
class UpdateSummaryWithNewAssetUseCaseTest {

    private final SummaryRepository summaryRepository = new SummaryInMemoryRepository();

    @Test
    void should_update_summary_money_value_with_a_created_asset() {
        // given
        SummaryId summaryId = SummaryId.random();
        Summary summary = new Summary(summaryId, UUID.randomUUID(), new Money(20), LocalDateTime.now(),
            SummaryState.DRAFT);
        summaryRepository.save(summary);
        Asset newAsset = new Asset(AssetId.random(), new Money(15), "item 1", List.of(), CUSTOM, summaryId);
        // when
        Summary result = new UpdateSummaryWithNewAssetUseCase(summaryRepository, newAsset, summaryId).execute();
        // then
        assertThat(result.getMoney()).isEqualTo(new Money(35));
    }
}