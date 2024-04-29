package personal.finance.summary.query;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.finance.summary.infrastracture.external.StockData;

public interface StockRepository extends JpaRepository<StockData, Long> {

}
