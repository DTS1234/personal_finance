package personal.finance.summary.domain;

import acceptance.GivenAssets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.model.Asset;
import personal.finance.summary.domain.model.Item;
import personal.finance.summary.domain.model.Summary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SummarySpec {

    private final SummaryFacade facade = new SummaryConfiguration().summaryFacade();

    @Test
    void shouldMoveSummaryFromDraftToConfirmed() {
        // given
        Summary newSummary = facade.createNewSummary();

        // when
        Summary confirmSummary = facade.confirmSummary(newSummary.getId());

        // then
        Assertions.assertThat(confirmSummary.getState())
            .isEqualTo(SummaryState.CONFIRMED);
    }

    @Test
    void shouldThrowIfNoSummaryFoundForGivenId() {
        assertThatThrownBy(() -> facade.confirmSummary(-1L))
            .hasMessage("Summary with id of -1 does not exist.");
    }

    @Test
    void should_throw_when_summary_is_not_in_draft() {
        // given
        Summary newSummary = facade.createNewSummary();
        facade.cancelSummary(newSummary.getId());

        // then - exception is thrown
        assertThatThrownBy(() -> facade.confirmSummary(newSummary.getId()))
            .hasMessage("Summary can be confirmed only if it is in DRAFT state.");
    }

    @Test
    void shouldCreateNewSummaryWithAllTheAssetsFromTheLastConfirmedSummary() {
        BigDecimal ten = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);

        // given - one cancelled summary
        Summary s1 = facade.createNewSummary();
        s1.cancel();
        // second updated and confirmed summary
        Summary s2 = facade.createNewSummary();
        s2.updateMoneyValue(ten);
        s2.addAsset(Asset.builder().id(1L)
            .name("Stock GPW")
            .moneyValue(ten)
            .items(List.of(Item.builder().moneyValue(ten)
                .quantity(BigDecimal.ONE)
                .name("Allegro")
                .id(1L)
                .build()))
            .buildAsset());

        Summary s2Updated = facade.updateSummaryInDraft(s2);
        facade.confirmSummary(s2Updated.getId());

        // when
        Summary execute = facade.createNewSummary();

        // then
        Assertions.assertThat(execute)
            .usingRecursiveComparison()
            .ignoringFields("assets.id", "assets.items.id")
            .isEqualTo(
                Summary.builder()
                    .id(3L)
                    .date(
                        execute.getDate()
                    )
                    .assets(
                        List.of(Asset.builder()
                            .id(2L)
                            .name("Stock GPW")
                            .moneyValue(ten)
                            .items(List.of(
                                Item.builder().moneyValue(ten)
                                    .quantity(BigDecimal.ONE)
                                    .name("Allegro")
                                    .id(1L)
                                    .build()
                            ))
                            .buildAsset())
                    )
                    .state(SummaryState.DRAFT)
                    .moneyValue(ten)
                    .build()
            );
    }

    @Test
    void shouldCreateDefaultEmptySummaryIfThereAreNoConfirmedSummaries() {
        Summary summary = facade.createNewSummary();

        Assertions.assertThat(summary)
            .usingRecursiveComparison()
            .ignoringFields("date")
            .isEqualTo(Summary.builder()
                .id(1L)
                .moneyValue(BigDecimal.ZERO)
                .assets(List.of())
                .state(SummaryState.DRAFT)
                .build());
    }

    @Test
    void should_update_summary_in_draft_with_new_asset_and_money_value() {
        // given
        Summary newSummary = facade.createNewSummary();

        // when - new asset is added
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(newSummary.getId())
            .assets(new ArrayList<>(updatedAssets))
            .date(newSummary.getDate())
            .state(SummaryState.DRAFT)
            .moneyValue(BigDecimal.valueOf(4211.67))
            .build();

        // and - summary is saved in draft
        Summary updated = facade.updateSummaryInDraft(updatedSummaryInDraft);

        // then - updated summary is saved
        Assertions.assertThat(updated)
            .usingRecursiveComparison()
            .isEqualTo(updatedSummaryInDraft);
    }

    @Test
    void should_throw_when_money_value_does_not_match_the_money_sum_of_all_items() {
        // given
        Summary newSummary = facade.createNewSummary();

        // when - new asset is added but money value is wrong
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(newSummary.getId())
            .assets(new ArrayList<>(updatedAssets))
            .date(newSummary.getDate())
            .state(SummaryState.DRAFT)
            .moneyValue(BigDecimal.valueOf(4200.67)) // should be 4211.67
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(updatedSummaryInDraft))
            .hasMessage("Invalid money value for the updated summary, it should be equal to the sum of all items money value.");
    }

    @Test
    void should_throw_when_summary_is_not_in_a_draft_mode() {
        // given
        Summary newSummary = facade.createNewSummary();
        Summary cancelledSummary = facade.cancelSummary(newSummary.getId());
        Summary secondSummary = facade.createNewSummary();
        Summary confirmedSummary = facade.confirmSummary(secondSummary.getId());

        cancelledSummary.addAsset(GivenAssets.newAssetOfTenWithOneItem());
        confirmedSummary.addAsset(GivenAssets.newAssetOfTenWithOneItem());

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(cancelledSummary))
            .hasMessage("Summary must be in a draft mode to be updated.");
        assertThatThrownBy(() -> facade.updateSummaryInDraft(confirmedSummary))
            .hasMessage("Summary must be in a draft mode to be updated.");
    }

    @Test
    void should_throw_when_summary_with_given_id_does_not_exist() {
        // when - id given is not correct
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(-1L)
            .assets(new ArrayList<>(updatedAssets))
            .date(LocalDateTime.now())
            .state(SummaryState.DRAFT)
            .moneyValue(BigDecimal.valueOf(4211.67))
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(updatedSummaryInDraft))
            .hasMessage("No summary with id: " + updatedSummaryInDraft.getId() + " exists.");
    }

    @Test
    void should_throw_when_summary_creation_date_is_different() {
        // given
        Summary newSummary = facade.createNewSummary();

        // when - new asset is added but id given is not correct
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(1L)
            .assets(new ArrayList<>(updatedAssets))
            .date(newSummary.getDate().minusDays(1))
            .state(SummaryState.DRAFT)
            .moneyValue(BigDecimal.valueOf(4211.67))
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(updatedSummaryInDraft))
            .hasMessage("Creation date cannot be changed for a summary.");
    }

}
