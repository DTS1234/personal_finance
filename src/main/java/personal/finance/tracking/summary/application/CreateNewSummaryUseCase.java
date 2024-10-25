package personal.finance.tracking.summary.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.common.events.EventPublisher;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.SummaryRepository;
import personal.finance.tracking.summary.domain.SummaryState;
import personal.finance.tracking.summary.domain.events.SummaryCreated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
class CreateNewSummaryUseCase implements UseCase<Summary> {

    private final UUID userId;
    private final SummaryRepository summaryRepository;
    private final EventPublisher eventPublisher;

    public Summary execute() {
        validatedIfThereAreNoDrafts();

        List<Summary> confirmedSummaries = summaryRepository.findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState.CONFIRMED, userId);
        SummaryId summaryId = SummaryId.random();

        if (!confirmedSummaries.isEmpty()) {
            Summary lastConfirmed = confirmedSummaries.getFirst();

            Summary summary = new Summary(
                summaryId,
                userId,
                lastConfirmed.getMoney(),
                LocalDateTime.now(),
                SummaryState.DRAFT);

            Summary newSummary = summaryRepository.save(summary);
            eventPublisher.publishEvent(new SummaryCreated(newSummary.getIdValue(), lastConfirmed.getIdValue(), UUID.randomUUID()));

            return newSummary;
        }

        Summary summarySaved = summaryRepository.save(Summary.builder()
            .id(summaryId)
            .userId(userId)
            .date(LocalDateTime.now())
            .state(SummaryState.DRAFT)
            .money(new Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)))
            .build());

        eventPublisher.publishEvent(new SummaryCreated(summarySaved.getIdValue(), null, UUID.randomUUID()));
        return summarySaved;
    }

    private void validatedIfThereAreNoDrafts() {
        List<Summary> summariesInDraft = summaryRepository.findSummaryByUserIdAndState(userId,
            SummaryState.DRAFT);
        if (!summariesInDraft.isEmpty()) {
            throw new IllegalStateException("User can have only one summary in creation.");
        }
    }
}
