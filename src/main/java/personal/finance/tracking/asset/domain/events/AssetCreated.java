package personal.finance.tracking.asset.domain.events;

import personal.finance.common.events.Event;
import personal.finance.tracking.asset.domain.Asset;

import java.time.Instant;
import java.util.UUID;

public class AssetCreated implements Event {

    public final UUID summaryId;
    public final Asset asset;
    public final Instant timestamp;
    public final UUID eventId;

    public AssetCreated(UUID summaryId, Asset asset, Instant timestamp, UUID eventId) {
        this.summaryId = summaryId;
        this.asset = asset;
        this.timestamp = timestamp;
        this.eventId = eventId;
    }
}
