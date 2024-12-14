package personal.finance.iam.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import personal.finance.iam.domain.SubscriptionType;

import java.time.LocalDate;

public record UserSubscriptionDTO(
    SubscriptionType subscriptionType,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate expiresAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate start,
    String status) {

}
