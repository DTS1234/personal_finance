package personal.finance.summary.infrastracture.web;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.summary.application.SummaryFacade;
import personal.finance.summary.application.dto.DTOMapper;
import personal.finance.summary.application.dto.SummaryDTO;

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
    public SummaryDTO updateSummaryInDraft(@PathVariable String id, @PathVariable String userId,
        @RequestBody SummaryDTO summaryDTO) {
        return DTOMapper.dto(facade.updateSummaryInDraft(summaryDTO, UUID.fromString(userId)));
    }
    
    @PostMapping("/{userId}/summaries/{summaryId}/confirm")
    public SummaryDTO confirmSummary(@PathVariable String summaryId, @PathVariable String userId) {
        return DTOMapper.dto(facade.confirmSummary(Long.valueOf(summaryId), UUID.fromString(userId)));
    }

    @PostMapping("/{userId}/summaries/new")
    public SummaryDTO createNewSummary(@PathVariable String userId) {
        return DTOMapper.dto(facade.createNewSummary(UUID.fromString(userId)));
    }

    @GetMapping("/{userId}/summaries")
    public List<SummaryDTO> getSummaries(@PathVariable UUID userId) {
        return facade.getSummaries(userId).stream()
            .map(DTOMapper::dto)
            .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/summaries/current")
    public SummaryDTO getCurrentDraft(@PathVariable UUID userId) {
        return DTOMapper.dto(facade.getCurrentDraft(userId));
    }
}
