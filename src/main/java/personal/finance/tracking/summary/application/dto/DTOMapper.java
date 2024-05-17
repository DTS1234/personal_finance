package personal.finance.tracking.summary.application.dto;

import personal.finance.tracking.summary.domain.Currency;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.Summary;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.infrastracture.persistance.entity.SummaryEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Collections.emptyList;

public class DTOMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static SummaryDTO dto(Summary summary) {
        return new SummaryDTO(
            summary.getId().getValue(),
            summary.getUserId(),
            summary.getMoney().getMoneyValue(),
            summary.getMoney().getCurrency(),
            summary.getState(),
            DATE_FORMATTER.format(summary.getDate()),
            emptyList()
        );
    }

    public static SummaryDTO dto(SummaryEntity summary) {
        return new SummaryDTO(
            summary.getId(),
            summary.getUserId(),
            summary.getMoneyValue(),
            Currency.EUR,
            summary.getState(),
            DATE_FORMATTER.format(summary.getDate()),
            emptyList()
        );
    }

    public static Summary from(SummaryDTO summaryDTO) {
        return new Summary(
            new SummaryId(summaryDTO.id),
            summaryDTO.userId,
            new Money(summaryDTO.money, summaryDTO.currency),
            LocalDateTime.parse(summaryDTO.date, DATE_FORMATTER),
            summaryDTO.state
        );
    }
}
