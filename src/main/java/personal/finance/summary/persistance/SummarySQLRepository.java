package personal.finance.summary.persistance;

import org.springframework.stereotype.Component;
import personal.finance.summary.domain.SummaryRepository;

@Component
public class SummaryJpaRepository implements SummaryRepository {
    private final SummarySQLRepository summarySQLRepository;
}
