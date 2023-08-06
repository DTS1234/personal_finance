package personal.finance.summary.usecase;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.model.SummaryEntity;

import static personal.finance.summary.usecase.CommonCode.moneyValueIsInconsistent;
import static personal.finance.summary.usecase.CommonCode.summaryHasDifferentCreationDate;

@RequiredArgsConstructor
public class ConfirmSummaryUseCase implements UseCase<SummaryEntity> {

    private final SummaryRepository summaryRepository;
    private final SummaryEntity toBeConfirmed;

    @Override
    public SummaryEntity execute() {

        Long id = toBeConfirmed.getId();
        SummaryEntity summaryFound = summaryRepository.findById(id).orElse(null);
        if (summaryFound == null) {
            throw new IllegalStateException("Summary with id of " + id + " does not exist.");
        }

        if (summaryHasDifferentCreationDate(summaryFound, toBeConfirmed)) {
            throw new IllegalStateException("Creation date cannot be changed for a summary.");
        }

        if (moneyValueIsInconsistent(toBeConfirmed)) {
            throw new IllegalStateException("Invalid money value for the summary, it should be equal to the sum of all items money value.");
        }

        if(!toBeConfirmed.isInDraft() || !summaryFound.isInDraft()) {
            throw new IllegalStateException("Summary can be confirmed only if it is in DRAFT state.");
        }

        SummaryEntity unsavedConfirmed = toBeConfirmed.confirm();
        return summaryRepository.save(unsavedConfirmed);
    }
}
