package personal.finance.iam.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Entity
@NoArgsConstructor
public class UserSubscription {
    @EmbeddedId
    private UserSubscriptionId userSubscriptionId;

    @Getter
    private LocalDate start;

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expires;

    @Getter
    private SubscriptionType subscriptionType;

    @Getter
    private SubscriptionStatus status;

    @OneToOne
    @Getter
    private User user;

    public UserSubscriptionId getId() {
        return userSubscriptionId;
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return this.expires.isAfter(now)
            && (this.start.isBefore(now) || this.start.isEqual(now));
    }

}
