package personal.finance.payment.domain.events;

import org.springframework.context.ApplicationEvent;
import personal.finance.iam.domain.SubscriptionType;

import java.time.LocalDate;
import java.util.UUID;

public class SubscriptionCreated extends ApplicationEvent {

    public LocalDate start;
    public LocalDate expiresAt;
    public SubscriptionType type;
    public UUID userId;

    public SubscriptionCreated(Object source, SubscriptionType type, UUID userId) {
        super(source);
        this.start = LocalDate.now();
        this.expiresAt = LocalDate.now().plusMonths(1);
        this.type = type;
        this.userId = userId;
    }

}
