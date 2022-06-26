package personal.finance.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    @Query("select SUM(a.moneyValue) FROM Asset a")
    double selectAssetMoneyValueSum();
}
