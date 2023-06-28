package personal.finance.summary.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.asset.Asset;
import personal.finance.asset.AssetMapper;
import personal.finance.asset.AssetRepository;
import personal.finance.summary.SummaryMapper;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PersistenceAdapter {

    private final SummaryRepository summaryRepository;
    private final AssetRepository assetRepository;

    public SummaryEntity addAssetToSummary(AssetDomain asset, SummaryDomain summary) {
        Long summaryId = summary.getId();
        SummaryEntity summaryEntity = summaryRepository.findById(summaryId).orElseThrow();

        if (summary.getMoneyValue().compareTo(summaryEntity.getMoneyValue()) != 0) {
            summaryEntity.updateMoneyValue(summary.getMoneyValue());
        }

        Asset newAssetEntity = AssetMapper.toEntity(asset);
        summaryEntity.addAsset(newAssetEntity);

        return summaryRepository.save(summaryEntity);
    }

    public boolean exists(AssetDomain asset) {
        return assetRepository.findById(asset.getId()).isPresent();
    }

    public boolean exists(SummaryDomain summary) {
        return summaryRepository.findById(summary.getId()).isPresent();
    }

    public SummaryDomain updateAsset(SummaryDomain summaryDomain, AssetDomain asset) {

        assetRepository.save(AssetMapper.toEntity(asset));

        return summaryDomain;
    }
}
