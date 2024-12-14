package personal.finance.iam.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class UserId implements Serializable {

    @Column(name = "user_id")
    public UUID value;

    public UserId(UUID value) {
        this.value = value;
    }

    public UserId() {
    }

    public static UserId fromString(String value) {
        return new UserId(UUID.fromString(value));
    }

    public static UserId random() {
        return new UserId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that instanceof UserId
            && Objects.equals(this.value, ((UserId) that).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}