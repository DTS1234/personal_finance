package personal.finance.payment.infrastracture.persistance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import personal.finance.payment.domain.Customer;
import personal.finance.payment.domain.CustomerId;
import personal.finance.payment.domain.CustomerRepository;

@Component
@RequiredArgsConstructor
public class CustomerRepositorySql implements CustomerRepository {

    private final CustomerRepositoryJpa repositoryJpa;

    @Override
    public Customer save(Customer customer) {
        return repositoryJpa.save(customer);
    }

    @Override
    public Customer findById(CustomerId customerId) {
        return repositoryJpa.findById(customerId).orElse(null);
    }
}
