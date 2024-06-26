package personal.finance.tracking.summary.application;

import lombok.extern.slf4j.Slf4j;
import personal.finance.tracking.summary.domain.Summary;

import java.time.temporal.ChronoUnit;

@Slf4j
public class InvariantsUtils {

    public static boolean summaryHasDifferentCreationDate(Summary originalSummary, Summary otherSummary) {
        return !originalSummary.getDate().truncatedTo(ChronoUnit.MINUTES
        ).isEqual(otherSummary.getDate().truncatedTo(ChronoUnit.MINUTES));
    }

}
