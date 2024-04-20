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


}
