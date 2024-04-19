package personal.finance.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @EmbeddedId
    @Column(name = "id", nullable = false)
    private CustomerId id;
    @Column(nullable = false)
    private String email;
    @Column(name = "customer_stripe_id")
    private String customerStripeId;
    @Column(name = "payment_method_id")
    private String paymentMethodId;

    public static Customer create() {
        return new Customer(CustomerId.random(), null, null, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Customer customer = (Customer) o;

        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
