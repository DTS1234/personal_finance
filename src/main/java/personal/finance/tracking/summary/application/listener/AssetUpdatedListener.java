package personal.finance.tracking.summary.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import personal.finance.common.events.EventListener;
import personal.finance.tracking.asset.domain.events.AssetUpdated;
import personal.finance.tracking.summary.application.UpdateSummaryWithUpdatedAsset;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.SummaryRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssetUpdatedListener implements EventListener<AssetUpdated> {

    private final SummaryRepository summaryRepository;

    @Override
    @org.springframework.context.event.EventListener
    public void handle(AssetUpdated event) {
        log.info("Received asset updated event.");
        new UpdateSummaryWithUpdatedAsset(summaryRepository, new SummaryId(event.summaryId), event.oldAsset, event.newAsset).execute();
    }
}
