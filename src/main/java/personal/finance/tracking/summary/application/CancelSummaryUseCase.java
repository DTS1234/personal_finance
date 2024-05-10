package personal.finance.tracking.summary.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.tracking.summary.domain.SummaryRepository;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryId;

import java.util.UUID;

@RequiredArgsConstructor
class CancelSummaryUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final SummaryId summaryId;
    private final UUID userId;

    @Override
    public Summary execute() {

        Summary summaryFound = summaryRepository.findByIdAndUserId(summaryId.getValue(), userId);

        if (summaryFound == null) {
            throw new IllegalStateException("This user does not have a summary with id of " + summaryId.getValue());
        }

        if (summaryFound.isCancelled()) {
            throw new IllegalStateException("Summary already cancelled");
        }

        Summary cancelled = summaryFound.cancel();

        return summaryRepository.save(cancelled);
    }
}
