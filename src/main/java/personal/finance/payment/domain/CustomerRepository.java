package personal.finance.payment.domain;

public interface CustomerRepository {
    Customer save(Customer customer);
    Customer findById(CustomerId customerId);
}
