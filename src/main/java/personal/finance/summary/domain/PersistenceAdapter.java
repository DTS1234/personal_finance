package personal.finance.summary.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.asset.Asset;
import personal.finance.asset.AssetMapper;
import personal.finance.asset.AssetRepository;
import personal.finance.asset.item.ItemRepository;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;
import personal.finance.summary.persistance.SummaryEntity;
import personal.finance.summary.persistance.SummaryRepository;

@Service
public class PersistenceAdapter {

    private final SummaryRepository summaryRepository;
    private final AssetRepository assetRepository;
    private final ItemRepository itemRepository;

    public PersistenceAdapter(SummaryRepository summaryRepository, AssetRepository assetRepository, ItemRepository itemRepository) {
        this.summaryRepository = summaryRepository;
        this.assetRepository = assetRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void addAssetToSummary(AssetDomain asset, SummaryDomain summary) {
        Long summaryId = summary.getId();
        SummaryEntity summaryEntity = summaryRepository.findById(summaryId).get();

        Asset newAssetEntity = AssetMapper.toEntity(asset);
        newAssetEntity.getItems().forEach(item -> item.setAsset(newAssetEntity));
        newAssetEntity.setSummary(summaryEntity);


        assetRepository.save(newAssetEntity);
        summaryEntity.getAssets().add(newAssetEntity);
        summaryRepository.save(summaryEntity);
    }

    public boolean exists(AssetDomain asset) {
        return assetRepository.findById(asset.getId()).isPresent();
    }
}
