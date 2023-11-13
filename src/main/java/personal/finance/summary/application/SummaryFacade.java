package personal.finance.summary.application;

import personal.finance.summary.application.dto.SummaryDTO;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.domain.SummaryId;

import java.util.List;

public class SummaryFacade {

    private final SummaryRepository summaryRepository;

    public SummaryFacade(SummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    public Summary confirmSummary(Long summaryId, Long userId) {
        return new ConfirmSummaryUseCase(summaryRepository, summaryId, userId).execute();
    }

    public Summary createNewSummary(Long userId) {
        return new CreateNewSummaryUseCase(userId, summaryRepository).execute();
    }

    public Summary updateSummaryInDraft(SummaryDTO updatedSummary, Long userId) {
        return new UpdateSummaryInDraftUseCase(summaryRepository, updatedSummary, userId).execute();
    }

    public Summary cancelSummary(Long id) {
        return new CancelSummaryUseCase(summaryRepository, new SummaryId(id)).execute();
    }

    public List<Summary> getSummaries(Long userId) {
        return summaryRepository.findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState.CONFIRMED, userId);
    }
}
