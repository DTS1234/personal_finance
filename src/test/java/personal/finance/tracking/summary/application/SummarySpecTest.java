package personal.finance.tracking.summary.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import personal.finance.GivenAssets;
import personal.finance.common.events.FakeEventPublisher;
import personal.finance.tracking.summary.application.dto.DTOMapper;
import personal.finance.tracking.summary.application.exceptions.NoSummaryInDraftException;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.SummaryState;
import personal.finance.tracking.summary.domain.events.SummaryCreated;
import personal.finance.tracking.summary.infrastracture.external.CurrencyFakeProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SummarySpecTest {

    private final SummaryFacade facade = new SummaryConfiguration().summaryFacadeTest();

    @BeforeAll
    static void setUp() {
        CurrencyManager.currencies = new CurrencyFakeProvider().getRates();
    }

    @Test
    void shouldMoveSummaryFromDraftToConfirmed() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // when
        Summary confirmSummary = facade.confirmSummary(newSummary.getId().getValue(), userId);

        // then
        Assertions.assertThat(confirmSummary.getState())
            .isEqualTo(SummaryState.CONFIRMED);
    }

    @Test
    void shouldThrowIfNoSummaryFoundForGivenId() {
        UUID summaryId = UUID.randomUUID();
        assertThatThrownBy(() -> facade.confirmSummary(summaryId, UUID.randomUUID()))
            .hasMessage("Summary with id of " + summaryId + " does not exist for this user.");
    }

    @Test
    void should_throw_when_summary_is_not_in_draft() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);
        facade.cancelSummary(newSummary.getIdValue(), userId);

        // then - exception is thrown
        assertThatThrownBy(() -> facade.confirmSummary(newSummary.getIdValue(), userId))
            .hasMessage("Summary can be confirmed only if it is in DRAFT state.");
    }

    @Test
    void shouldThrowIfUserTriesToConfirmOtherSummaryThanHis() {
        // given
        UUID u1 = UUID.randomUUID();
        UUID u2 = UUID.randomUUID();
        Summary summaryFirstUser = facade.createNewSummary(u1);
        facade.createNewSummary(u2);

        assertThatThrownBy(() -> facade.confirmSummary(summaryFirstUser.getId().getValue(), u2))
            .hasMessage("Summary with id of " + summaryFirstUser.getIdValue() + " does not exist for this user.");
    }

    @Test
    void should_publish_correct_event_for_each_new_summary_creation() {
        // given - one cancelled summary
        UUID userId = UUID.randomUUID();
        Summary s1 = facade.createNewSummary(userId);
        s1.confirm();
        // second updated and confirmed summary
        Summary s2 = facade.createNewSummary(userId);
        facade.confirmSummary(s2.getId().getValue(), userId);

        FakeEventPublisher eventPublisher = (FakeEventPublisher) facade.getEventPublisher();

        // when
        assertThat(eventPublisher.publishedStore).hasSize(2);
        SummaryCreated actual = (SummaryCreated)eventPublisher.publishedStore.getFirst();
        assertThat(actual.newSummaryId).isEqualTo(s1.getIdValue());
        assertThat(actual.previousSummaryId).isNull();
        SummaryCreated actual2 = (SummaryCreated)eventPublisher.publishedStore.get(1);
        assertThat(actual2.newSummaryId).isEqualTo(s2.getIdValue());
        assertThat(actual2.previousSummaryId).isEqualTo(s1.getIdValue());
    }

    @Test
    void shouldCreateDefaultEmptySummaryIfThereAreNoConfirmedSummaries() {
        UUID userId = UUID.randomUUID();
        Summary summary = facade.createNewSummary(userId);

        // summary is returned
        Assertions.assertThat(summary)
            .usingRecursiveComparison()
            .ignoringFields("date")
            .isEqualTo(Summary.builder()
                .id(summary.getId())
                .userId(userId)
                .money(new Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)))
                .assets(List.of())
                .state(SummaryState.DRAFT)
                .build());


        // and event is published
        FakeEventPublisher eventPublisher = (FakeEventPublisher) facade.getEventPublisher();
        SummaryCreated actual = (SummaryCreated)eventPublisher.publishedStore.getFirst();
        assertThat(actual.newSummaryId).isEqualTo(summary.getIdValue());
        assertThat(actual.previousSummaryId).isNull();
        assertThat(actual.eventID).isNotNull();
        assertThat(actual.timestamp).isNotNull();
    }

    @Test
    void should_update_summary_in_draft_with_new_asset_and_money_value() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

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
        Summary updated = facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), userId);

        // then - updated summary is saved
        Assertions.assertThat(updated)
            .usingRecursiveComparison()
            .ignoringFields("assets.items.id", "date", "assetsIds")
            .isEqualTo(updatedSummaryInDraft);

        Assertions.assertThat(updated.getDate().truncatedTo(ChronoUnit.MINUTES))
            .isEqualTo(newSummary.getDate().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void should_throw_when_money_value_does_not_match_the_money_sum_of_all_items() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

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
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), userId))
            .hasMessage(
                "Invalid money value for the updated summary, it should be equal to the sum of all items money value.");
    }

    @Test
    void should_throw_when_trying_to_update_a_summary_of_other_user() {
        // given
        UUID u1 = UUID.randomUUID();
        UUID u2 = UUID.randomUUID();
        Summary firstUserSummary = facade.createNewSummary(u1);
        facade.createNewSummary(u2);

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
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), u2))
            .hasMessage("Summary with id " + updatedSummaryInDraft.getId() + " does not exist for this user.");
    }

    @Test
    void should_throw_when_summary_is_not_in_a_draft_mode() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);
        Summary cancelledSummary = facade.cancelSummary(newSummary.getIdValue(), userId);
        Summary secondSummary = facade.createNewSummary(userId);
        Summary confirmedSummary = facade.confirmSummary(secondSummary.getIdValue(), userId);

        // when
        cancelledSummary.addAsset(GivenAssets.newAssetOfTenWithOneItem());
        confirmedSummary.addAsset(GivenAssets.newAssetOfTenWithOneItem());

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(cancelledSummary), userId))
            .hasMessage("Summary must be in a draft mode to be updated.");
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(confirmedSummary), userId))
            .hasMessage("Summary must be in a draft mode to be updated.");
    }

    @Test
    void should_throw_when_summary_with_given_id_does_not_exist() {
        // when - id given is not correct
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        SummaryId summaryId = SummaryId.random();
        Summary updatedSummaryInDraft = Summary.builder()
            .id(summaryId)
            .assets(new ArrayList<>(updatedAssets))
            .date(LocalDateTime.now())
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4211.67)))
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), UUID.randomUUID()))
            .hasMessage("Summary with id " + summaryId.getValue() + " does not exist for this user.");
    }

    @Test
    void should_throw_when_summary_creation_date_is_different() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // when - new asset is added but id given is not correct
        List<Asset> updatedAssets = new ArrayList<>(GivenAssets.assets());
        updatedAssets.add(GivenAssets.newAssetOfTenWithOneItem());
        Summary updatedSummaryInDraft = Summary.builder()
            .id(new SummaryId(newSummary.getIdValue()))
            .assets(new ArrayList<>(updatedAssets))
            .date(newSummary.getDate().minusDays(1))
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.valueOf(4211.67)))
            .build();

        // then - exception is thrown
        assertThatThrownBy(() -> facade.updateSummaryInDraft(DTOMapper.dto(updatedSummaryInDraft), userId))
            .hasMessage("Creation date cannot be changed for a summary.");
    }

    @Test
    @DisplayName("should return current summary that is in draft for a given user")
    void shouldReturnSummaryInDraft() {
        //given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);

        // when
        Summary currentDraft = facade.getCurrentDraft(userId);

        // then
        assertThat(newSummary).isEqualTo(currentDraft);
    }

    @Test
    @DisplayName("should throw a No summary in draft exception if no summaries in draft for a user")
    void shouldThrowIfNoSummaryInDraft() {
        //given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);
        facade.cancelSummary(newSummary.getId().getValue(), userId);

        // when
        assertThatThrownBy(() -> facade.getCurrentDraft(userId))
            .hasMessage("No summary is being created for this user.")
            .isInstanceOf(NoSummaryInDraftException.class);
    }

    @Test
    @DisplayName("should throw if there user tries to create another summary without confirming or closing the previous one")
    void shouldThrowIfTwoSummariesAreInDraft() {
        //given
        UUID userId = UUID.randomUUID();
        facade.createNewSummary(userId);

        // when
        assertThatThrownBy(() -> facade.createNewSummary(userId))
            .hasMessage("User can have only one summary in creation.");
    }

    @Test
    void shouldCancelSummary() {
        // given
        UUID userId = UUID.randomUUID();
        Summary newSummary = facade.createNewSummary(userId);
        // when
        facade.cancelSummary(newSummary.getId().getValue(), userId);
        // then
        assertThatThrownBy(() -> facade.getCurrentDraft(userId));
    }

    @Test
    void shouldThrowWhenTryingToCancelOtherUserSummary() {
        // given
        UUID userId = UUID.randomUUID();
        UUID secondUserId = UUID.randomUUID();
        Summary firstSummary = facade.createNewSummary(userId);
        Summary secondSummary = facade.createNewSummary(secondUserId);
        // expect
        assertThatThrownBy(() -> facade.cancelSummary(firstSummary.getId().getValue(), secondUserId))
            .hasMessage("This user does not have a summary with id of " + firstSummary.getId());
    }
}
