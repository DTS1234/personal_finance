package personal.finance.summary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.summary.domain.SummaryFacade;
import personal.finance.summary.domain.model.Summary;

/**
 * @author akazmierczak
 * @create 18.06.2022
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class SummaryController {

    private final SummaryFacade facade;

    @PostMapping("/summaries/{id}/update")
    public Summary updateSummaryInDraft(@PathVariable String id, @RequestBody Summary summary) {
        return facade.updateSummaryInDraft(summary);
    }

    @PostMapping("/summaries/{id}/confirm")
    public Summary confirmSummary(@PathVariable String id) {
        return facade.confirmSummary(Long.valueOf(id));
    }

    @PostMapping("/summaries/new")
    public Summary createNewSummary() {
        return facade.createNewSummary();
    }

}
