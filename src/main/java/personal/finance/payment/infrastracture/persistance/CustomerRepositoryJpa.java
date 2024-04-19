package personal.finance.payment.infrastracture.persistance;

import org.springframework.data.repository.CrudRepository;
import personal.finance.payment.domain.Customer;
import personal.finance.payment.domain.CustomerId;

public interface CustomerRepositoryJpa extends CrudRepository<Customer, CustomerId> {

}
