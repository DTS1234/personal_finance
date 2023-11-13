package personal.finance.iam.infrastracture.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.finance.iam.domain.VerificationToken;

@Repository
public interface VerificationTokenRepositoryJpa extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    @Query("SELECT v FROM VerificationToken v WHERE v.user.email = ?1")
    VerificationToken findByUser(String username);
}
