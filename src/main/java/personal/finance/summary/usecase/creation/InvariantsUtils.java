package personal.finance.summary.usecase;

import personal.finance.summary.domain.model.SummaryEntity;

public class CommonCode {

    public static boolean summaryHasDifferentCreationDate(SummaryEntity originalSummary, SummaryEntity otherSummary) {
        return !originalSummary.getDate().isEqual(otherSummary.getDate());
    }

    public static boolean moneyValueIsInconsistent(SummaryEntity summaryEntity) {
        return summaryEntity.getMoneyValue().compareTo(summaryEntity.sumOfItemsMoneyValue()) != 0
            || summaryEntity.getMoneyValue().compareTo(summaryEntity.sumAssetsMoneyValue()) != 0;
    }
}
