package personal.finance.summary.application;

import personal.finance.summary.application.dto.SummaryDTO;
import personal.finance.summary.domain.Currency;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.domain.SummaryId;
import personal.finance.summary.domain.UserRepository;

import java.util.List;
import java.util.UUID;

public class SummaryFacade {

    private final SummaryRepository summaryRepository;
    private final UserRepository userRepository;

    public SummaryFacade(SummaryRepository summaryRepository, UserRepository userRepository) {
        this.summaryRepository = summaryRepository;
        this.userRepository = userRepository;
    }

    public Summary confirmSummary(UUID summaryId, UUID userId) {
        return new ConfirmSummaryUseCase(summaryRepository, summaryId, userId).execute();
    }

    public Summary createNewSummary(UUID userId) {
        return new CreateNewSummaryUseCase(userId, summaryRepository).execute();
    }

    public Summary updateSummaryInDraft(SummaryDTO updatedSummary, UUID userId) {
        return new UpdateSummaryInDraftUseCase(summaryRepository, updatedSummary, userId).execute();
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
