package personal.finance.summary.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;

@RequiredArgsConstructor
class CancelSummaryUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final SummaryId summaryId;

    @Override
    public Summary execute() {

        Summary summaryFound = summaryRepository.findById(summaryId);

        if (summaryFound == null) {
            throw new IllegalStateException("Summary with id of " + summaryId + " does not exist.");
        }

        if (summaryFound.isCancelled()) {
            throw new IllegalStateException("Summary already cancelled");
        }

        Summary cancelled = summaryFound.cancel();

        return summaryRepository.save(cancelled);
    }
}
