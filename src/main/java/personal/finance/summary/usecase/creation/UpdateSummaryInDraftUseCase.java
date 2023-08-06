package personal.finance.summary.usecase;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.domain.model.SummaryEntity;
import personal.finance.summary.domain.SummaryRepository;

import static personal.finance.summary.usecase.CommonCode.moneyValueIsInconsistent;
import static personal.finance.summary.usecase.CommonCode.summaryHasDifferentCreationDate;

@RequiredArgsConstructor
public class UpdateSummaryInDraftUseCase implements UseCase<SummaryEntity> {

    private final SummaryRepository summaryRepository;
    private final SummaryEntity newUpdatedSummary;

    @Override
    public SummaryEntity execute() {

        if (!newUpdatedSummary.isInDraft()) {
            throw new IllegalStateException("Summary must be in a draft mode to be updated.");
        }

        if (moneyValueIsInconsistent(newUpdatedSummary)) {
            throw new IllegalStateException("Invalid money value for the updated summary, it should be equal to the sum of all items money value.");
        }

        SummaryEntity originalSummary = findOriginalSummaryOrNull();
        if (originalSummary == null) {
            throw new IllegalStateException("No summary with id: " + newUpdatedSummary.getId() + " exists.");
        }

        if (summaryHasDifferentCreationDate(originalSummary, newUpdatedSummary)) {
            throw new IllegalStateException("Creation date cannot be changed for a summary.");
        }

        return summaryRepository.save(newUpdatedSummary);
    }

    private SummaryEntity findOriginalSummaryOrNull() {
        Long id = newUpdatedSummary.getId();
        return summaryRepository.findById(id).orElse(null);
    }
}
