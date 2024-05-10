package personal.finance.tracking.summary.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import personal.finance.tracking.asset.infrastracture.persistance.entity.AssetEntity;
import personal.finance.tracking.asset.infrastracture.persistance.repository.AssetJpaRepository;
import personal.finance.tracking.summary.application.dto.DTOMapper;
import personal.finance.tracking.summary.application.dto.SummaryDTO;
import personal.finance.tracking.summary.infrastracture.persistance.entity.SummaryEntity;
import personal.finance.tracking.summary.infrastracture.persistance.repository.SummaryJpaRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class SummaryProjection {

    private final SummaryJpaRepository jpaRepository;
    private final AssetJpaRepository assetJpaRepository;

    public Page<SummaryDTO> query(SearchCriteria searchCriteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findAll(new SummarySpecification(searchCriteria), pageable)
            .map(DTOMapper::dto);
    }

    public SummaryDTO findById(UUID summaryId) {
        SummaryEntity summaryEntity = jpaRepository.findById(summaryId).orElse(null);
        if (summaryEntity == null) {
            throw new IllegalStateException("Summary not found for this id " + summaryId);
        }
        log.info("Looking for all assets for summary: {}", summaryId);
        List<AssetEntity> assetsFoundForSummary = assetJpaRepository.findAllBySummaryId(summaryId);
        log.info("All assets retrived: {}", assetsFoundForSummary);
        assetsFoundForSummary.forEach(summaryEntity::addAsset);

        return DTOMapper.dto(summaryEntity);
    }
}
