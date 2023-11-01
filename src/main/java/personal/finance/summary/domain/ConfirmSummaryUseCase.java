package personal.finance.summary.domain;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.domain.model.Summary;
import personal.finance.summary.domain.usecase.UseCase;

@RequiredArgsConstructor
class ConfirmSummaryUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final Long summaryId;

    @Override
    public Summary execute() {

        Summary summaryFound = summaryRepository.findById(summaryId).orElse(null);
        if (summaryFound == null) {
            throw new IllegalStateException("Summary with id of " + summaryId + " does not exist.");
        }

        if (!summaryFound.isInDraft()) {
            throw new IllegalStateException("Summary can be confirmed only if it is in DRAFT state.");
        }

        if(summaryFound.sumAssetsMoneyValue().compareTo(summaryFound.getMoneyValue()) != 0 ||
        summaryFound.sumOfItemsMoneyValue().compareTo(summaryFound.getMoneyValue()) != 0) {
            throw new IllegalStateException("Invalid money value for the summary, it should be equal to the sum of all items money value.");
        }

        Summary unsavedConfirmed = summaryFound.confirm();
        return summaryRepository.save(unsavedConfirmed);
    }
}
