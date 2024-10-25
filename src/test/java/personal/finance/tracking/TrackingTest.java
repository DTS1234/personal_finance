package personal.finance.tracking;

import integration.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import personal.finance.tracking.asset.application.AssetConfiguration;
import personal.finance.tracking.asset.application.AssetFacade;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetType;
import personal.finance.tracking.summary.application.SummaryConfiguration;
import personal.finance.tracking.summary.application.SummaryFacade;
import personal.finance.tracking.summary.domain.Currency;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TrackingTest extends IntegrationTest {

    @Autowired
    private SummaryFacade summaryFacade;
    @Autowired
    private AssetFacade assetFacade;

    @Test
    void should_keep_money_value_consistent() {
        //given
        UUID userId = UUID.randomUUID();

        // when
        Summary newSummary = summaryFacade.createNewSummary(userId);
        // and asset added of 100
        assetFacade.createAsset(newSummary.getIdValue(), new Asset(null, new Money(100, Currency.EUR), "Asset 1", List.of(),
            AssetType.CUSTOM, newSummary.getId()));
        // and confirmed
        Summary confirmedSummary = summaryFacade.confirmSummary(newSummary.getIdValue(), userId);
        // expect
        assertThat(confirmedSummary.getMoney()).isEqualTo(new Money(100, Currency.EUR));

        // and when
        Summary secondSummary = summaryFacade.createNewSummary(userId);
        // and new asset of 50
        assetFacade.createAsset(secondSummary.getIdValue(), new Asset(null, new Money(50, Currency.EUR), "Asset 2", List.of(),
            AssetType.CUSTOM, secondSummary.getId()));
        // and confirmed
        Summary secondConfirmed = summaryFacade.confirmSummary(secondSummary.getIdValue(), userId);
        // expect
        assertThat(secondConfirmed.getMoney()).isEqualTo(new Money(150, Currency.EUR));
    }
}
