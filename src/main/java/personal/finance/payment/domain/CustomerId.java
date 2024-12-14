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

    public String getStringValue() {
        return value.toString();
    }

    public CustomerId(UUID id) {
        this.value = id;
    }

    public static CustomerId random() {
        return new CustomerId(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        CustomerId that = (CustomerId) object;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
