package personal.finance.tracking.summary.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.SummaryRepository;

@RequiredArgsConstructor
public class UpdateSummaryWithNewAssetUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final Asset newAsset;
    private final SummaryId summaryId;

    @Override
    public Summary execute() {
        Summary summary = this.summaryRepository.findById(summaryId.getValue());
        summary.updateMoneyValue(summary.getMoney().add(this.newAsset.getMoney()));
        this.summaryRepository.save(summary);
        return summary;
    }
}
