package personal.finance.tracking.summary.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import personal.finance.tracking.asset.application.AssetDTOMapper;
import personal.finance.tracking.asset.infrastracture.persistance.entity.AssetEntity;
import personal.finance.tracking.asset.infrastracture.persistance.repository.AssetJpaRepository;
import personal.finance.tracking.asset.application.AssetDTO;
import personal.finance.tracking.summary.application.dto.DTOMapper;
import personal.finance.tracking.summary.application.dto.SummaryDTO;
import personal.finance.tracking.summary.application.exceptions.NoSummaryInDraftException;
import personal.finance.tracking.summary.domain.SummaryState;
import personal.finance.tracking.summary.infrastracture.persistance.entity.SummaryEntity;
import personal.finance.tracking.summary.infrastracture.persistance.repository.SummaryJpaRepository;
import personal.finance.tracking.summary.infrastracture.persistance.repository.UserSummaryRepositoryJpa;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class SummaryProjection {

    private final SummaryJpaRepository jpaRepository;
    private final AssetJpaRepository assetJpaRepository;
    private final UserSummaryRepositoryJpa userSummaryRepositoryJpa;

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
        log.info("All assets retrieved: {}", assetsFoundForSummary);

        SummaryDTO dto = DTOMapper.dto(summaryEntity);
        dto.assets = assetsFoundForSummary.stream().map(AssetDTOMapper::dto).toList();
        return dto;
    }

    public SummaryDTO getCurrentDraft(UUID userID) {
        List<SummaryDTO> summary = jpaRepository.findSummaryByUserIdAndStateEquals(userID, SummaryState.DRAFT).stream()
            .map(DTOMapper::dto).toList();

        if (summary.isEmpty()) {
            throw new NoSummaryInDraftException("No summary is being created for this user.");
        }

        SummaryDTO first = summary.getFirst();

        first.assets = assetJpaRepository.findAllBySummaryId(first.id).stream().map(AssetDTOMapper::dto)
            .collect(Collectors.toList());

        return first;
    }

    public List<SummaryDTO> getConfirmedSummaries(UUID userId) {
        return jpaRepository.findSummaryByStateEqualsAndUserIdOrderByDateDesc(SummaryState.CONFIRMED, userId).stream().map(
            s -> {
                List<AssetDTO> assets = assetJpaRepository.findAllBySummaryId(s.getId()).stream().map(AssetDTOMapper::dto)
                    .toList();
                BigDecimal money = assets.stream().map(a -> a.money).reduce(BigDecimal.ZERO, BigDecimal::add);
                money = money.setScale(4, RoundingMode.HALF_UP);
                SummaryDTO dto = DTOMapper.dto(s);
                dto.money = money;
                dto.assets = assets;
                return dto;
            }
        ).toList();
    }
}
