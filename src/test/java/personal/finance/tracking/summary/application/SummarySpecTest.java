package personal.finance.tracking.summary.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import personal.finance.common.events.FakeEventPublisher;
import personal.finance.tracking.summary.application.exceptions.NoSummaryInDraftException;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryState;
import personal.finance.tracking.summary.domain.events.SummaryCreated;
import personal.finance.tracking.summary.infrastracture.external.CurrencyFakeProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        SummaryCreated actual = (SummaryCreated) eventPublisher.publishedStore.getFirst();
        assertThat(actual.newSummaryId).isEqualTo(s1.getIdValue());
        assertThat(actual.previousSummaryId).isNull();
        SummaryCreated actual2 = (SummaryCreated) eventPublisher.publishedStore.get(1);
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
                .state(SummaryState.DRAFT)
                .build());

        // and event is published
        FakeEventPublisher eventPublisher = (FakeEventPublisher) facade.getEventPublisher();
        SummaryCreated actual = (SummaryCreated) eventPublisher.publishedStore.getFirst();
        assertThat(actual.newSummaryId).isEqualTo(summary.getIdValue());
        assertThat(actual.previousSummaryId).isNull();
        assertThat(actual.eventID).isNotNull();
        assertThat(actual.timestamp).isNotNull();
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
