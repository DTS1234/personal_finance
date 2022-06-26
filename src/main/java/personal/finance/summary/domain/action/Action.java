package personal.finance.summary.domain.action;

import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;

public interface Action {
    void execute(AssetDomain asset, SummaryDomain summary);
}
