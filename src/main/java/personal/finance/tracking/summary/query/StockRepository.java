package personal.finance.tracking.summary.query;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.finance.tracking.summary.infrastracture.external.StockData;

public interface StockRepository extends JpaRepository<StockData, Long> {

}
