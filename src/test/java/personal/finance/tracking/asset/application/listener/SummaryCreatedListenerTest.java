package personal.finance.tracking.asset.application.listener;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import personal.finance.tracking.asset.infrastracture.persistance.repository.AssetInMemoryRepository;
import personal.finance.tracking.summary.domain.events.SummaryCreated;

import java.util.UUID;

@DisplayNameGeneration(ReplaceUnderscores.class)
class SummaryCreatedListenerTest {

    private SummaryCreatedListener summaryCreatedListener = new SummaryCreatedListener(new AssetInMemoryRepository().clear());

    @Test
    void should_create_asset_for_new_summary() {
        UUID newSummaryId = UUID.randomUUID();
        UUID previousSummaryId = UUID.randomUUID();
        SummaryCreated summaryCreated = new SummaryCreated(newSummaryId, previousSummaryId, UUID.randomUUID());

        summaryCreatedListener.handle(summaryCreated);


    }

}