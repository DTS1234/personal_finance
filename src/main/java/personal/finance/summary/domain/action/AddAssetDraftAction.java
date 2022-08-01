package personal.finance.summary.domain.action;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.SummaryMapper;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.PersistenceAdapter;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class AddAssetDraftAction implements Action {

    private final PersistenceAdapter persistenceAdapter;

    @Override
    public SummaryDomain execute(AssetDomain asset, SummaryDomain summary) {
        if (summary.getState().compareTo(SummaryState.DRAFT) != 0) {
            throw new IllegalStateException(String.format("You cannot add that asset in %s state.", summary.getState()));
        }
        if (doesAssetWithThatNameAlreadyExist(asset.getName(), summary)) {
            throw new AssetWithThatNameAlreadyExistsException(asset.getName());
        }
        summary.getAssets().add(asset);
        BigDecimal sum = summary.getAssets().stream().map(AssetDomain::getMoneyValue).reduce(BigDecimal.valueOf(0), BigDecimal::add);
        summary.setMoneyValue(sum);
        return SummaryMapper.toDomain(persistenceAdapter.addAssetToSummary(asset, summary));
    }

    private boolean doesAssetWithThatNameAlreadyExist(String assetName, SummaryDomain summary) {
        return summary.getAssets().stream().map(AssetDomain::getName).anyMatch(name -> name.equals(assetName));
    }
}
