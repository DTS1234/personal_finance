package personal.finance.tracking.asset.application;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.infrastracture.persistance.repository.AssetInMemoryRepository;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.SummaryId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CreateNewSummaryAssetsUseCaseTest {


    AssetInMemoryRepository repository = new AssetInMemoryRepository();

    @Test
    void should_copy_assets_for_new_summary() {
        SummaryId previousConfirmedSummary = SummaryId.random();
        SummaryId newSummaryId = SummaryId.random();

        Asset given = new Asset(AssetId.random(), new Money(0), "name", List.of(), previousConfirmedSummary);
        repository.saveAll(List.of(given));

        List<Asset> assets = new CreateNewSummaryAssetsUseCase(repository, previousConfirmedSummary,
            newSummaryId).execute();

        assertThat(assets).hasSize(1);
        assertThat(assets.getFirst())
            .usingRecursiveComparison()
            .ignoringFields("summaryId", "id")
            .isEqualTo(given);
        assertThat(assets.getFirst().getSummaryId()).isEqualTo(newSummaryId);
    }
}