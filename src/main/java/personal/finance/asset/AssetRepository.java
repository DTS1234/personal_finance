package personal.finance.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.finance.summary.domain.model.AssetEntity;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
    @Query("select SUM(a.moneyValue) FROM AssetEntity a")
    double selectAssetMoneyValueSum();
}
