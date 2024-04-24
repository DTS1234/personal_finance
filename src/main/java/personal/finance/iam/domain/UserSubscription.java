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

@Entity
@NoArgsConstructor
public class UserSubscription {
    @EmbeddedId
    @Setter
    private UserSubscriptionId userSubscriptionId;

    @Setter
    @Getter
    private LocalDate start;

    @Setter
    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expires;

    @Getter
    @Setter
    private SubscriptionType subscriptionType;

    @OneToOne
    @Setter
    private User user;

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return this.expires.isAfter(now)
            && (this.start.isBefore(now) || this.start.isEqual(now));
    }

}
