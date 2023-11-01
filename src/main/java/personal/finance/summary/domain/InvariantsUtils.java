package personal.finance.summary.domain;

import personal.finance.summary.domain.model.Summary;

public class InvariantsUtils {

    public static boolean summaryHasDifferentCreationDate(Summary originalSummary, Summary otherSummary) {
        return !originalSummary.getDate().isEqual(otherSummary.getDate());
    }

    public static boolean moneyValueIsInconsistent(Summary summary) {
        return summary.getMoneyValue().compareTo(summary.sumOfItemsMoneyValue()) != 0
            || summary.getMoneyValue().compareTo(summary.sumAssetsMoneyValue()) != 0;
    }
}
