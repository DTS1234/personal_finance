package personal.finance.tracking.asset.infrastracture.web;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.tracking.asset.application.AssetDTOMapper;
import personal.finance.tracking.asset.application.AssetFacade;
import personal.finance.tracking.summary.application.dto.AssetDTO;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Timed(percentiles = {0.5, 0.75, 0.95, 0.99})
public class AssetController {

    private final AssetFacade facade;

    @PostMapping("/{userId}/summaries/{summaryId}/asset/{assetId}")
    public AssetDTO updateAsset(@PathVariable("userId") UUID userId, @PathVariable("summaryId") UUID summaryId, @PathVariable("assetId") UUID assetId, @RequestBody AssetDTO assetDTO) {
        return AssetDTOMapper.dto(facade.updateAsset(userId, summaryId, assetId, AssetDTOMapper.from(assetDTO)));
    }

    @PostMapping("/{userId}/summaries/{summaryId}/asset/add")
    public AssetDTO addAsset(@PathVariable("userId") UUID userId, @PathVariable("summaryId") UUID summaryId, @RequestBody
        AssetDTO assetDTO) {
        return AssetDTOMapper.dto(facade.createAsset(summaryId, AssetDTOMapper.from(assetDTO)));
    }

}
