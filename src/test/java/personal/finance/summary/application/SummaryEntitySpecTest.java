package personal.finance.summary.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import personal.finance.GivenAssets;
import personal.finance.summary.application.dto.DTOMapper;
import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.AssetId;
import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.ItemId;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;
import personal.finance.summary.domain.SummaryState;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SummaryEntitySpecTest {

    private final SummaryFacade facade = new SummaryConfiguration().summaryFacadeTest();

    @Test
    void shouldMoveSummaryFromDraftToConfirmed() {
        // given
        Summary newSummary = facade.createNewSummary(1L);

        // when
        Summary confirmSummary = facade.confirmSummary(newSummary.getId().getValue(), 1L);

        // then
        Assertions.assertThat(confirmSummary.getState())
            .isEqualTo(SummaryState.CONFIRMED);
    }

    @Test
    void shouldThrowIfNoSummaryFoundForGivenId() {
        assertThatThrownBy(() -> facade.confirmSummary(-1L, 1L))
            .hasMessage("Summary with id of -1 does not exist for this user.");
    }

    @Test
    void should_throw_when_summary_is_not_in_draft() {
        // given
        Summary newSummary = facade.createNewSummary(1L);
        facade.cancelSummary(1L);

        // then - exception is thrown
        assertThatThrownBy(() -> facade.confirmSummary(1L, 1L))
            .hasMessage("Summary can be confirmed only if it is in DRAFT state.");
    }

    @Test
    void shouldThrowIfUserTriesToConfirmOtherSummaryThanHis() {
        // given
        Summary summaryFirstUser = facade.createNewSummary(1L);
        Summary summarySecondUser = facade.createNewSummary(2L);

        assertThatThrownBy(() -> facade.confirmSummary(1L, 2L))
            .hasMessage("Summary with id of 1 does not exist for this user.");
    }


    @Test
    void shouldCreateNewSummaryWithAllTheAssetsFromTheLastConfirmedSummary() {
        BigDecimal ten = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);

        // given - one cancelled summary
        Summary s1 = facade.createNewSummary(1L);
        s1.cancel();
        // second updated and confirmed summary
        Summary s2 = facade.createNewSummary(1L);
        s2.updateMoneyValue(new Money(ten));
        s2.addAsset(Asset.builder().id(new AssetId(1L))
            .name("Stock GPW")
            .money(new Money(ten))
            .items(List.of(Item.builder().money(new Money(ten))
                .quantity(BigDecimal.ONE)
                .name("Allegro")
                .id(new ItemId(1L))
                .build()))
            .buildAsset());

        Summary s2Updated = facade.updateSummaryInDraft(DTOMapper.dto(s2), 1L);
        facade.confirmSummary(s2.getId().getValue(), 1L);

        // when
        Summary result = facade.createNewSummary(1L);

        // then
        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("assets.id", "assets.items.id")
            .isEqualTo(
                Summary.builder()
                    .id(result.getId())
                    .userId(1L)
                    .date(
                        result.getDate()
                    )
                    .assets(
                        List.of(Asset.builder()
                            .id(new AssetId(2L))
                            .name("Stock GPW")
                            .money(new Money(ten))
                            .items(List.of(
                                Item.builder().money(new Money(ten))
                                    .quantity(BigDecimal.ONE)
                                    .name("Allegro")
                                    .id(new ItemId(1L))
                                    .build()
                            ))
                            .buildAsset())
                    )
                    .state(SummaryState.DRAFT)
                    .money(new Money(ten))
                    .build()
            );
    }

    @Test
    void shouldCreateDefaultEmptySummaryIfThereAreNoConfirmedSummaries() {
        Summary summary = facade.createNewSummary(1L);

        Assertions.assertThat(summary)
            .usingRecursiveComparison()
            .ignoringFields("date")
            .isEqualTo(Summary.builder()
                .id(new SummaryId(1L))
                .userId(1L)
                .money(new Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)))
                .assets(List.of())
                .state(SummaryState.DRAFT)
                .build());
    }

    @Test
    void should_update_summary_in_draft_with_new_asset_and_money_value() {
        // given
        Summary newSummary = facade.createNewSummary(1L);

        // when - new asset is added
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(newSummary.getId())
            .assets(new ArrayList<>(updatedAssets))
            .date(newSummary.getDate())
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4211.67)))
            .build();

        // and - summary is saved in draft
        Summary updated = facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), 1L);

        // then - updated summary is saved
        Assertions.assertThat(updated)
            .usingRecursiveComparison().ignoringFields("assets.items.id")
            .isEqualTo(updatedSummaryInDraft);
    }

    @Test
    void should_throw_when_money_value_does_not_match_the_money_sum_of_all_items() {
        // given
        Summary newSummary = facade.createNewSummary(1L);

        // when - new asset is added but money value is wrong
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(newSummary.getId())
            .assets(new ArrayList<>(updatedAssets))
            .date(newSummary.getDate())
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4200.67))) // should be 4211.67
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), 1L))
            .hasMessage(
                "Invalid money value for the updated summary, it should be equal to the sum of all items money value.");
    }

    @Test
    void should_throw_when_trying_to_update_a_summary_of_other_user() {
        // given
        Summary firstUserSummary = facade.createNewSummary(1L);
        Summary secondUserSummary = facade.createNewSummary(2L);

        // when
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(firstUserSummary.getId())
            .assets(new ArrayList<>(updatedAssets))
            .date(firstUserSummary.getDate())
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4211.67))) // should be 4211.67
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), 2L))
            .hasMessage("Summary with id " + updatedSummaryInDraft.getId() + " does not exist for this user.");
    }

    @Test
    void should_throw_when_summary_is_not_in_a_draft_mode() {
        // given
        Summary newSummary = facade.createNewSummary(1L);
        Summary cancelledSummary = facade.cancelSummary(1L);
        Summary secondSummary = facade.createNewSummary(1L);
        Summary confirmedSummary = facade.confirmSummary(2L, 1L);

        // when
        cancelledSummary.addAsset(GivenAssets.newAssetOfTenWithOneItem());
        confirmedSummary.addAsset(GivenAssets.newAssetOfTenWithOneItem());

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(cancelledSummary), 1L))
            .hasMessage("Summary must be in a draft mode to be updated.");
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(confirmedSummary), 1L))
            .hasMessage("Summary must be in a draft mode to be updated.");
    }

    @Test
    void should_throw_when_summary_with_given_id_does_not_exist() {
        // when - id given is not correct
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(new SummaryId(-1L))
            .assets(new ArrayList<>(updatedAssets))
            .date(LocalDateTime.now())
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4211.67)))
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), 1L))
            .hasMessage("Summary with id -1 does not exist for this user.");
    }

    @Test
    void should_throw_when_summary_creation_date_is_different() {
        // given
        Summary newSummary = facade.createNewSummary(1L);

        // when - new asset is added but id given is not correct
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(new SummaryId(1L))
            .assets(new ArrayList<>(updatedAssets))
            .date(newSummary.getDate().minusDays(1))
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4211.67)))
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), 1L))
            .hasMessage("Creation date cannot be changed for a summary.");
    }
}
