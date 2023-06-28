package personal.finance.summary.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.finance.summary.SummaryState;

import java.util.List;

/**
 * @author akazmierczak
 * @create 18.06.2022
 */
@Repository
public interface SummaryRepository extends JpaRepository<SummaryEntity, Long> {
    
    List<SummaryEntity> findSummaryByStateEqualsOrderById(SummaryState state);
    List<SummaryEntity> findSummaryByStateEqualsOrderByDateDesc(SummaryState state);

}
