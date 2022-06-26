package personal.finance.summary.domain.action;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.PersistenceAdapter;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class ChangeAssetDraftAction implements Action {

    private final PersistenceAdapter persistenceAdapter;

    @Override
    public void execute(AssetDomain asset, SummaryDomain summary) {
        if (summary.getState().compareTo(SummaryState.DRAFT) != 0){
            throw new IllegalStateException(String.format("Cannot edit summary in that state: %s.", summary.getState()));
        }
        if (!persistenceAdapter.exists(asset)){
            throw new AssetDoesNotExistException(String.format("Asset with id of %s, does not exist.", asset.getId()));
        }
        if (asset.getMoneyValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money value cannot be negative!");
        }

    }
}
