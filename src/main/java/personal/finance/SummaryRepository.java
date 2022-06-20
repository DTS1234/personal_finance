package personal.finance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author akazmierczak
 * @create 18.06.2022
 */
@Repository
public interface SummaryRepository extends JpaRepository<Summary, Long> {
}
