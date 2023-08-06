package personal.finance.summary.usecase;

import org.junit.jupiter.api.Test;

class AddAssetToSummaryInDraftUseCaseTest {

    @Test
    void should_update_summary_in_draft_with_new_asset_and_money_value() {
        new AddAssetToSummaryInDraftUseCase().execute();
    }

}