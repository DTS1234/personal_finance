package personal.finance.tracking.summary.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import personal.finance.common.events.EventListener;
import personal.finance.tracking.asset.domain.events.AssetCreated;
import personal.finance.tracking.summary.application.UpdateSummaryWithNewAssetUseCase;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.SummaryRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssetCreatedListener implements EventListener<AssetCreated> {

    private final SummaryRepository summaryRepository;

    @Override
    @org.springframework.context.event.EventListener
    public void handle(AssetCreated event) {
        log.info("Received asset created event.");
        new UpdateSummaryWithNewAssetUseCase(summaryRepository, event.asset, new SummaryId(event.summaryId)).execute();
    }
}
