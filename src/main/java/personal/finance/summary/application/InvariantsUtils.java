package personal.finance.summary.application;

import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;

public class InvariantsUtils {

    public static boolean summaryHasDifferentCreationDate(Summary originalSummary, Summary otherSummary) {
        return !originalSummary.getDate().isEqual(otherSummary.getDate());
    }

    public static boolean moneyValueIsInconsistent(Summary summary) {
        return !summary.getMoney().equals(new Money(summary.sumOfItemsMoneyValue()))
            || !summary.getMoney().equals(new Money(summary.sumAssetsMoneyValue()));
    }
}
