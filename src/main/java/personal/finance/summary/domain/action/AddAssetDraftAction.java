package personal.finance.summary.domain.action;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.PersistenceAdapter;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;

@RequiredArgsConstructor
public class AddAssetDraftAction implements Action {

    private final PersistenceAdapter persistenceAdapter;

    @Override
    public void execute(AssetDomain asset, SummaryDomain summary) {
        if (summary.getState().compareTo(SummaryState.DRAFT) != 0) {
            throw new IllegalStateException(String.format("You cannot add that asset in %s state.", summary.getState()));
        }
        if (doesAssetWithThatNameAlreadyExist(asset.getName(), summary)) {
            throw new AssetWithThatNameAlreadyExistsException(asset.getName());
        }

        summary.getAssets().add(asset);

        persistenceAdapter.addAssetToSummary(asset, summary);
    }

    private boolean doesAssetWithThatNameAlreadyExist(String assetName, SummaryDomain summary) {
        return summary.getAssets().stream().map(AssetDomain::getName).anyMatch(name -> name.equals(assetName));
    }
}
