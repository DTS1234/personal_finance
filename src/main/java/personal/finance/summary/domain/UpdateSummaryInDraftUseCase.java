package personal.finance.summary.domain;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.domain.model.Summary;

import static personal.finance.summary.domain.InvariantsUtils.moneyValueIsInconsistent;
import static personal.finance.summary.domain.InvariantsUtils.summaryHasDifferentCreationDate;

@RequiredArgsConstructor
class UpdateSummaryInDraftUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final Summary newUpdatedSummary;

    @Override
    public Summary execute() {

        if (!newUpdatedSummary.isInDraft()) {
            throw new IllegalStateException("Summary must be in a draft mode to be updated.");
        }

        if (moneyValueIsInconsistent(newUpdatedSummary)) {
            throw new IllegalStateException("Invalid money value for the updated summary, it should be equal to the sum of all items money value.");
        }

        Summary originalSummary = findOriginalSummaryOrNull();
        if (originalSummary == null) {
            throw new IllegalStateException("No summary with id: " + newUpdatedSummary.getId() + " exists.");
        }

        if (summaryHasDifferentCreationDate(originalSummary, newUpdatedSummary)) {
            throw new IllegalStateException("Creation date cannot be changed for a summary.");
        }

        return summaryRepository.save(newUpdatedSummary);
    }

    private Summary findOriginalSummaryOrNull() {
        Long id = newUpdatedSummary.getId();
        return summaryRepository.findById(id).orElse(null);
    }
}
