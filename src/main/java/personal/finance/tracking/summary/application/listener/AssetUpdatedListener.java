package personal.finance.tracking.summary.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import personal.finance.common.events.EventListener;
import personal.finance.tracking.asset.domain.events.AssetUpdated;
import personal.finance.tracking.summary.application.SummaryFacade;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssetUpdatedListener implements EventListener<AssetUpdated> {

    private final SummaryFacade summaryFacade;

    @Override
    @org.springframework.context.event.EventListener
    public void handle(AssetUpdated event) {
        log.info("Received asset updated event.");
    }
}
