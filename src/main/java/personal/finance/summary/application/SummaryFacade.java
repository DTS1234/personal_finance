package personal.finance.summary.application;

import personal.finance.summary.application.dto.SummaryDTO;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.domain.SummaryId;

import java.util.List;
import java.util.UUID;

public class SummaryFacade {

    private final SummaryRepository summaryRepository;

    public SummaryFacade(SummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    public Summary confirmSummary(Long summaryId, UUID userId) {
        return new ConfirmSummaryUseCase(summaryRepository, summaryId, userId).execute();
    }

    public Summary createNewSummary(UUID userId) {
        return new CreateNewSummaryUseCase(userId, summaryRepository).execute();
    }

    public Summary updateSummaryInDraft(SummaryDTO updatedSummary, UUID userId) {
        return new UpdateSummaryInDraftUseCase(summaryRepository, updatedSummary, userId).execute();
    }

    public Summary cancelSummary(Long id) {
        return new CancelSummaryUseCase(summaryRepository, new SummaryId(id)).execute();
    }

    public List<Summary> getSummaries(UUID userId) {
        return summaryRepository.findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState.CONFIRMED, userId);
    }

    public Summary getCurrentDraft(UUID userId) {
        return new GetCurrentDraftUseCase(summaryRepository, userId).execute();
    }
}
