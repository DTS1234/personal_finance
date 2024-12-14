package personal.finance.payment.application;

import personal.finance.payment.domain.Customer;
import personal.finance.payment.domain.CustomerId;
import personal.finance.payment.domain.CustomerRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<CustomerId, Customer> customers = new HashMap<>();

    @Override
    public Customer save(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }

    @Override
    public Customer findById(CustomerId customerId) {
        return customers.get(customerId);
    }
}
