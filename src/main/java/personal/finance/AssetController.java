package personal.finance;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class AssetController {

    private final AssetRepository assetRepository;
    private final AssetService assetService;

    @GetMapping("/assets")
    public List<Asset> getAssets() {
        return assetRepository.findAll();
    }

    @GetMapping("/assets/sum")
    public double sumAssetMoneyValue() {
        return assetRepository.selectAssetMoneyValueSum();
    }

    @GetMapping("/assets/percentages")
    public Map<Long, Double> getPercentages() {
        return assetService.getPercentages();
    }

}
