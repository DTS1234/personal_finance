package personal.finance.iam.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class UserSubscription {
    @EmbeddedId
    private UserSubscriptionId userSubscriptionId;
    @Setter
    @Getter
    private LocalDateTime start;
    @Setter
    @Getter
    private LocalDateTime expires;
    @Getter
    @Setter
    private SubscriptionType subscriptionType;

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return this.expires.isAfter(now)
            && (this.start.isBefore(now) || this.start.isEqual(now));
    }

}
