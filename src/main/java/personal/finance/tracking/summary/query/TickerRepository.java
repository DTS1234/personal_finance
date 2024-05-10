package personal.finance.tracking.summary.query;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.finance.tracking.summary.infrastracture.external.TickerData;

import java.util.UUID;

public interface TickerRepository extends JpaRepository<TickerData, UUID> {

}
