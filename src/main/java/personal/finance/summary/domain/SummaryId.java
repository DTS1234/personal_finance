package personal.finance.summary.domain;

import lombok.Value;

@Value
public class SummaryId {

    Long value;

    @Override
    public String toString() {
        return value.toString();
    }
}
