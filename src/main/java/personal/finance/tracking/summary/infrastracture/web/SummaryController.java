package personal.finance.tracking.summary.infrastracture.web;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.tracking.summary.application.SummaryFacade;
import personal.finance.tracking.summary.application.dto.DTOMapper;
import personal.finance.tracking.summary.application.dto.SummaryDTO;
import personal.finance.tracking.summary.application.dto.UpdateCurrencyDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author akazmierczak
 * @create 18.06.2022
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
@Timed(percentiles = {0.5, 0.75, 0.95, 0.99})
public class SummaryController {

    private final SummaryFacade facade;

    @PostMapping("/{userId}/summaries/{id}/update")
    public SummaryDTO updateSummaryInDraft(@PathVariable("id") String id, @PathVariable("userId") String userId,
        @RequestBody SummaryDTO summaryDTO) {
        return DTOMapper.dto(facade.updateSummaryInDraft(summaryDTO, UUID.fromString(userId)));
    }

    @PostMapping("/{userId}/summaries/{summaryId}/confirm")
    public SummaryDTO confirmSummary(@PathVariable("summaryId") String summaryId, @PathVariable("userId") String userId) {
        return DTOMapper.dto(facade.confirmSummary(UUID.fromString(summaryId), UUID.fromString(userId)));
    }

    @PostMapping("/{userId}/summaries/new")
    public SummaryDTO createNewSummary(@PathVariable("userId") String userId) {
        return DTOMapper.dto(facade.createNewSummary(UUID.fromString(userId)));
    }

    @GetMapping("/{userId}/summaries")
    public List<SummaryDTO> getSummaries(@PathVariable("userId") UUID userId) {
        return facade.getConfirmedSummaries(userId).stream()
            .map(DTOMapper::dto)
            .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/summaries/current")
    public SummaryDTO getCurrentDraft(@PathVariable("userId") UUID userId) {
        return DTOMapper.dto(facade.getCurrentDraft(userId));
    }

    @PostMapping("/{userId}/summaries/{summaryId}/cancel")
    public SummaryDTO cancelSummary(@PathVariable("userId") UUID userId, @PathVariable("summaryId") String summaryId) {
        return DTOMapper.dto(facade.cancelSummary(UUID.fromString(summaryId), userId));
    }

    @PostMapping("/{userId}/currency")
    public String updateCurrency(@RequestBody UpdateCurrencyDTO updateDTO) {
        return facade.updateCurrency(UUID.fromString(updateDTO.userId()), updateDTO.currency()).name();
    }
}
