package personal.finance.tracking.summary.application;

import lombok.RequiredArgsConstructor;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.common.UseCase;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.SummaryRepository;

@RequiredArgsConstructor
public class UpdateSummaryWithUpdatedAsset implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final SummaryId summaryId;
    private final Asset oldAsset;
    private final Asset newAsset;

    @Override
    public Summary execute() {
        Summary summaryFound = summaryRepository.findById(summaryId.getValue());
        if (summaryFound == null) {
            throw new IllegalStateException("Summary with id of " + summaryId.getValue() + " not found.");
        }
        Money valueToAdd = newAsset.getMoney().subtract(oldAsset.getMoney());
        Money newValue = summaryFound.getMoney().add(valueToAdd);
        summaryFound.updateMoneyValue(newValue);
        return summaryRepository.save(summaryFound);
    }
}
