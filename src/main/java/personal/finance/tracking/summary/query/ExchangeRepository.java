package personal.finance.tracking.summary.query;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.finance.tracking.summary.infrastracture.external.ExchangeData;

import java.util.Optional;
import java.util.UUID;

public interface ExchangeRepository extends JpaRepository<ExchangeData, UUID> {

    Optional<ExchangeData> findByCode(String Code);
}
