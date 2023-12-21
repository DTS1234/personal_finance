package personal.finance.summary.infrastracture.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.finance.summary.infrastracture.persistance.entity.UserEntity;

@Repository
public interface UserSummaryRepositoryJpa extends JpaRepository<UserEntity, String> {

}
