package personal.finance.tracking.summary.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class UserId implements Serializable {

    public UUID value;

    public UserId(UUID value) {
        this.value = value;
    }

    public UserId() {
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
        return this == that || that instanceof personal.finance.iam.domain.UserId
            && Objects.equals(this.value, ((personal.finance.iam.domain.UserId) that).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
