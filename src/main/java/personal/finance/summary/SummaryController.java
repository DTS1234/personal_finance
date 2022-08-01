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

    @GetMapping("/summaries")
    public List<Summary> getConfirmedSummaries() {
        return summaryService.getAllConfirmedSummaries();
    }

    @GetMapping("/summaries/{id}/available_assets")
    public List<Asset> getAvailableSummaries(@PathVariable Long id) {
        return summaryService.getAvailableAssets(id);
    }

    @PostMapping("/summaries/{id}/add_asset")
    public Summary addAssetToSummaryDraft(@PathVariable Long id, @RequestBody Asset asset) {
        return summaryService.addAsset(id, asset);
    }

    @PostMapping("/summaries/{id}/confirm")
    public Summary confirmSummary(@PathVariable Long id) {
        return summaryService.confirmSummary(id);
    }

    @PostMapping("/summaries/{summaryId}/assets/{assetId}")
    public Summary confirmSummary(@PathVariable Long summaryId, @PathVariable Long assetId, @RequestBody Asset editedAsset) {
        return summaryService.editAsset(summaryId, assetId, editedAsset);
    }

    @PostMapping("/summaries/new")
    public Summary createNewSummary(@RequestBody Summary summary) {
        return summaryService.createNewSummary(summary);
    }

}
