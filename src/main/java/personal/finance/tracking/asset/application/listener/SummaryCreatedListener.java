package personal.finance.tracking.asset.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import personal.finance.common.events.EventListener;
import personal.finance.tracking.asset.application.CreateNewSummaryAssetsUseCase;
import personal.finance.tracking.asset.domain.AssetRepository;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.events.SummaryCreated;

@Component
@RequiredArgsConstructor
@Slf4j
public class SummaryCreatedListener implements EventListener<SummaryCreated> {

    private final AssetRepository assetRepository;

    @Override
    @org.springframework.context.event.EventListener
    public void handle(SummaryCreated event) {
        log.info("Handling summary created, creating new assets ...");
        new CreateNewSummaryAssetsUseCase(assetRepository, new SummaryId(event.previousSummaryId), new SummaryId(event.newSummaryId)).execute();
    }
}
