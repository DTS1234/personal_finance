package personal.finance.summary.infrastracture.web;

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
import personal.finance.summary.domain.Summary;

import java.util.List;

/**
 * @author akazmierczak
 * @create 18.06.2022
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class SummaryController {

    private final SummaryFacade facade;

    @PostMapping("/{userId}/summaries/{id}/update")
    public SummaryDTO updateSummaryInDraft(@PathVariable String id, @PathVariable String userId,
        @RequestBody SummaryDTO summaryDTO) {
        return DTOMapper.dto(facade.updateSummaryInDraft(summaryDTO, Long.valueOf(userId)));
    }

    @PostMapping("/{userId}/summaries/{summaryId}/confirm")
    public SummaryDTO confirmSummary(@PathVariable String summaryId, @PathVariable String userId) {
        return DTOMapper.dto(facade.confirmSummary(Long.valueOf(summaryId), Long.valueOf(userId)));
    }

    @PostMapping("/{userId}/summaries/new")
    public SummaryDTO createNewSummary(@PathVariable String userId) {
        return DTOMapper.dto(facade.createNewSummary(Long.valueOf(userId)));
    }

    @GetMapping("/{userId}/summaries")
    public List<Summary> getSummaries(@PathVariable String userId) {
        return facade.getSummaries(Long.valueOf(userId));
    }

}
