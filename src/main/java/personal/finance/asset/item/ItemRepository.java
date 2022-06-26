package personal.finance.asset.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
