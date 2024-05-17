package personal.finance.tracking.asset.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetRepository;
import personal.finance.tracking.summary.domain.SummaryId;

import java.util.List;

@RequiredArgsConstructor
public class CreateNewSummaryAssetsUseCase implements UseCase<List<Asset>> {

    private final AssetRepository assetRepository;
    private final SummaryId previousConfirmedSummary;
    private final SummaryId newSummaryId;

    @Override
    public List<Asset> execute() {
        List<Asset> assetsToCopy = assetRepository.findAllBySummaryId(previousConfirmedSummary.getValue());
        List<Asset> newAssets = assetsToCopy.stream().map(a -> a.newCopyForSummary(newSummaryId)).toList();
        return assetRepository.saveAll(newAssets);
    }
}
