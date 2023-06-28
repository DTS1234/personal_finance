package personal.finance.summary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import personal.finance.asset.Asset;
import personal.finance.summary.output.Summary;

import java.util.List;

/**
 * @author akazmierczak
 * @create 18.06.2022
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping("/summaries/current")
    public Summary getCurrentSummary() {
        return SummaryMapper.toSummary(summaryService.getLatestConfirmedSummary());
    }

    @GetMapping("/summaries")
    public List<Summary> getConfirmedSummaries() {
        return summaryService.getAllConfirmedSummaries();
    }

    @GetMapping("/summaries/{id}/available_assets")
    public List<Asset> getAvailableSummaries(@PathVariable Long id) {
        return summaryService.getAvailableAssets(id);
    }

    @GetMapping("/summaries/{id}")
    public Summary getSummary(@PathVariable String id) {
        return summaryService.getSummary(Long.parseLong(id));
    }

    @PostMapping("/summaries/{id}/update")
    public Summary updateSummaryInDraft(@PathVariable String id, @RequestBody Summary summary) {
        System.out.println(id);
        return summaryService.updateSummaryInDraft(summary);
    }

    @PostMapping("/summaries/{id}/updateAsset/{assetId}")
    public Summary updateSummaryAssetInDraft(@PathVariable String id, @PathVariable String assetId, @RequestBody Asset asset) {
        return summaryService.updateSummaryAssetInDraft(Long.parseLong(id), Long.parseLong(assetId), asset);
    }

    @PostMapping("/summaries/{id}/confirm")
    public Summary confirmSummary(@PathVariable Long id) {
        return summaryService.confirmSummary(id);
    }

    @PostMapping("/summaries/new")
    public Summary createNewSummary(@RequestBody Summary summary) {
        return summaryService.createNewSummary(summary);
    }

}
