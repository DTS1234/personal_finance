package personal.finance.summary.application;

import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;

import java.time.temporal.ChronoUnit;

public class InvariantsUtils {

    public static boolean summaryHasDifferentCreationDate(Summary originalSummary, Summary otherSummary) {
        return !originalSummary.getDate().truncatedTo(ChronoUnit.MINUTES
        ).isEqual(otherSummary.getDate().truncatedTo(ChronoUnit.MINUTES));
    }

    public static boolean moneyValueIsInconsistent(Summary summary) {
        return !summary.getMoney().equals(new Money(summary.sumOfItemsMoneyValue()))
            || !summary.getMoney().equals(new Money(summary.sumAssetsMoneyValue()));
    }
}
