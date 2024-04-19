package personal.finance.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class CustomerId implements Serializable {

    @Serial
    private static final long serialVersionUID = 2808337917235213703L;

    @Column(name = "customer_id")
    UUID value;

    public CustomerId() {

    }

    public CustomerId(UUID id) {
        this.value = id;
    }

    public static CustomerId random() {
        return new CustomerId(UUID.randomUUID());
    }
}
