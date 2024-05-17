package personal.finance.tracking.asset.domain.events;

import personal.finance.common.events.Event;
import personal.finance.tracking.asset.domain.Asset;

import java.time.Instant;
import java.util.UUID;

public class AssetUpdated implements Event {
    public final UUID summaryId;
    public final Asset newAsset;
    public final Asset oldAsset;
    public final Instant timestamp;
    public final UUID eventId;

    public AssetUpdated(UUID summaryId, Asset asset, Asset oldAsset, Instant timestamp, UUID eventId) {
        this.summaryId = summaryId;
        this.newAsset = asset;
        this.oldAsset = oldAsset;
        this.timestamp = timestamp;
        this.eventId = eventId;
    }
}
