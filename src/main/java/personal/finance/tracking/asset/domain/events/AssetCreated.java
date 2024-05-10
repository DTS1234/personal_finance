package personal.finance.tracking.asset.domain.events;

import personal.finance.common.events.Event;

import java.time.Instant;
import java.util.UUID;

public class AssetCreated implements Event {

    public final UUID summaryId;
    public final UUID assetId;
    public final Instant timestamp;
    public final UUID eventId;

    public AssetCreated(UUID summaryId, UUID assetId, Instant timestamp, UUID eventId) {
        this.summaryId = summaryId;
        this.assetId = assetId;
        this.timestamp = timestamp;
        this.eventId = eventId;
    }
}
