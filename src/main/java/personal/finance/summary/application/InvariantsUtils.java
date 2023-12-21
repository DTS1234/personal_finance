package personal.finance.summary.application;

import lombok.extern.slf4j.Slf4j;
import personal.finance.summary.domain.Summary;

import java.time.temporal.ChronoUnit;

@Slf4j
public class InvariantsUtils {

    public static boolean summaryHasDifferentCreationDate(Summary originalSummary, Summary otherSummary) {
        return !originalSummary.getDate().truncatedTo(ChronoUnit.MINUTES
        ).isEqual(otherSummary.getDate().truncatedTo(ChronoUnit.MINUTES));
    }

    public static boolean moneyValueIsInconsistent(Summary summary) {

        log.debug("CHECKING MONEY VALUES:");
        log.debug("Summary money value: " + summary.getMoney());
        log.debug("Items money value: " + summary.sumItemsMoney());
        log.debug("Assets money value: " + summary.sumAssetsMoney());

        return !summary.getMoney().equals(summary.sumItemsMoney())
            || !summary.getMoney().equals(summary.sumAssetsMoney());
    }
}
