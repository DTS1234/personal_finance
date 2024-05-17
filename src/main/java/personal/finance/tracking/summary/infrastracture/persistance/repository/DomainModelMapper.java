package personal.finance.tracking.summary.infrastracture.persistance.repository;

import lombok.RequiredArgsConstructor;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.infrastracture.persistance.entity.SummaryEntity;

import java.util.UUID;

@RequiredArgsConstructor
class DomainModelMapper {

    Summary map(SummaryEntity summaryEntity) {
        return new Summary(
            new SummaryId(summaryEntity.getId()),
            summaryEntity.getUserId(),
            new Money(summaryEntity.getMoneyValue()),
            summaryEntity.getDate(),
            summaryEntity.getState()
        );
    }

    SummaryEntity map(Summary summary) {
        return new SummaryEntity(getIdValue(summary), summary.getUserId(), summary.getMoney().getMoneyValue(),
            summary.getDate(), summary.getState());
    }

    private static UUID getIdValue(Summary summary) {
        SummaryId id = summary.getId();
        if (id == null) {
            return null;
        }
        return id.getValue();
    }
}
