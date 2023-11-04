package personal.finance.summary.domain;

import personal.finance.summary.domain.model.Summary;
import personal.finance.summary.domain.model.SummaryState;

import java.util.List;
import java.util.Set;

public class SummaryFacade {

    private final SummaryRepository summaryRepository;

    public SummaryFacade(SummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    public Summary confirmSummary(Long id) {
        return new ConfirmSummaryUseCase(summaryRepository, id).execute();
    }

    public Summary createNewSummary() {
        return new CreateNewSummaryUseCase(summaryRepository).execute();
    }

    public Summary updateSummaryInDraft(Summary updatedSummary) {
        return new UpdateSummaryInDraftUseCase(summaryRepository, updatedSummary).execute();
    }

    public Summary cancelSummary(Long id) {
        return new CancelSummaryUseCase(summaryRepository, id).execute();
    }

    public List<Summary> getSummaries() {
        return summaryRepository.findSummaryByStateEqualsOrderById(SummaryState.CONFIRMED);
    }
}
