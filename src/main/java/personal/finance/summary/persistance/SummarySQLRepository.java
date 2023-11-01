package personal.finance.summary.persistance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import personal.finance.summary.domain.model.Summary;
import personal.finance.summary.domain.model.SummaryState;
import personal.finance.summary.domain.SummaryRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SummarySQLRepository implements SummaryRepository {
    private final SummaryJpaRepository jpaRepository;

    @Override
    public Summary save(Summary summary) {
        return jpaRepository.save(summary);
    }

    @Override
    public List<Summary> saveAll(List<Summary> entityList) {
        return jpaRepository.saveAll(entityList);
    }


    @Override
    public Optional<Summary> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    public List<Summary> findSummaryByStateEqualsOrderById(SummaryState summaryState) {
        return jpaRepository.findSummaryByStateEqualsOrderById(summaryState);
    }

    public List<Summary> findSummaryByStateEqualsOrderByDateDesc(SummaryState summaryState) {
        return jpaRepository.findSummaryByStateEqualsOrderByDateDesc(summaryState);
    }

}
