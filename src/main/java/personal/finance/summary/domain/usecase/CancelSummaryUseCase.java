package personal.finance.summary.domain.usecase;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.domain.SummaryRepository;
import personal.finance.summary.domain.model.Summary;

@RequiredArgsConstructor
public class CancelSummaryUseCase implements UseCase<Summary> {

    private final SummaryRepository summaryRepository;
    private final Long summaryId;

    @Override
    public Summary execute() {

        Summary summaryFound = summaryRepository.findById(summaryId).orElse(null);

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
