package personal.finance.iam.domain;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionId {

    @Column(name = "subscription_id")
    public UUID value;

    public static UserSubscriptionId random() {
        return new UserSubscriptionId(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        UserSubscriptionId that = (UserSubscriptionId) object;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
