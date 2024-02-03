package personal.finance.summary.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class SummaryId {

    UUID value;

    public static SummaryId random() {
        return new SummaryId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
