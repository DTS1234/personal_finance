package personal.finance.tracking.summary.domain.events;

import personal.finance.common.events.Event;

import java.time.Instant;
import java.util.UUID;

public class SummaryCreated implements Event {
    public UUID newSummaryId;
    public UUID previousSummaryId;
    public UUID eventID;
    public Instant timestamp;

    public SummaryCreated(UUID newSummaryId, UUID previousSummaryId, UUID eventID) {
        this.newSummaryId = newSummaryId;
        this.previousSummaryId = previousSummaryId;
        this.eventID = eventID;
        this.timestamp = Instant.now();
    }
}
