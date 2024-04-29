package personal.finance.summary.query;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.finance.summary.infrastracture.external.TickerData;

import java.util.UUID;

public interface TickerRepository extends JpaRepository<TickerData, UUID> {

}
