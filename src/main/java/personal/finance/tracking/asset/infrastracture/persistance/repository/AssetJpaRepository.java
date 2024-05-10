package personal.finance.tracking.asset.infrastracture.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.finance.tracking.asset.infrastracture.persistance.entity.AssetEntity;

import java.util.List;
import java.util.UUID;

public interface AssetJpaRepository extends JpaRepository<AssetEntity, UUID> {

    List<AssetEntity> findAllBySummaryId(UUID summaryId);
}
