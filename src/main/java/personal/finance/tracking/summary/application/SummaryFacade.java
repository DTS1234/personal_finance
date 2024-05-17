package personal.finance.tracking.summary.application;

import lombok.Getter;
import personal.finance.common.events.EventPublisher;
import personal.finance.tracking.asset.domain.AssetRepository;
import personal.finance.tracking.summary.application.dto.SummaryDTO;
import personal.finance.tracking.summary.domain.Currency;
import personal.finance.tracking.summary.domain.SummaryRepository;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryState;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.UserRepository;

import java.util.List;
import java.util.UUID;

public class SummaryFacade {

    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;
    @Getter
    private final EventPublisher eventPublisher;

    public SummaryFacade(SummaryRepository summaryRepository, UserRepository userRepository,
        EventPublisher eventPublisher) {
        this.summaryRepository = summaryRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public Summary confirmSummary(UUID summaryId, UUID userId) {
        return new ConfirmSummaryUseCase(summaryRepository, summaryId, userId).execute();
    }

    public Summary createNewSummary(UUID userId) {
        return new CreateNewSummaryUseCase(userId, summaryRepository, eventPublisher).execute();
    }

    public Summary cancelSummary(UUID id, UUID userId) {
        return new CancelSummaryUseCase(summaryRepository, new SummaryId(id), userId).execute();
    }

    public List<Summary> getConfirmedSummaries(UUID userId) {
        return summaryRepository.findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState.CONFIRMED, userId);
    }

    public Summary getCurrentDraft(UUID userId) {
        return new GetCurrentDraftUseCase(summaryRepository, userId).execute();
    }

    public Currency updateCurrency(UUID userId, Currency currency) {
        return userRepository.updateCurrency(userId.toString(), currency);
    }
}
