package personal.finance.summary.infrastracture.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import personal.finance.summary.domain.Currency;

@Entity
@Table(name = "users_summary")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserEntity {

    @Id
    String userId;

    Currency currency;
}
