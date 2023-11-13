package personal.finance.summary.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;

@RequiredArgsConstructor
class ConfirmSummaryUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final Long summaryId;

    private final Long userId;

    @Override
    public Summary execute() {

        Summary summaryFound = summaryRepository.findByIdAndUserId(summaryId, userId);
        if (summaryFound == null) {
            throw new IllegalStateException("Summary with id of " + summaryId + " does not exist for this user.");
        }

        if (summaryFound.isInDraft()) {
            throw new IllegalStateException("Summary can be confirmed only if it is in DRAFT state.");
        }

        if (!new Money(summaryFound.sumAssetsMoneyValue()).equals(summaryFound.getMoney())
            || !new Money(summaryFound.sumOfItemsMoneyValue()).equals(summaryFound.getMoney())) {
            throw new IllegalStateException("Invalid money value for the summary, "
                + "it should be equal to the sum of all items money value.");
        }

        Summary unsavedConfirmed = summaryFound.confirm();
        return summaryRepository.save(unsavedConfirmed);
    }
}
