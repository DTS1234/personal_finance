package personal.finance.payment.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record SubscriptionCancelledConfirmationDTO(String customerId, String msg, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate expiryDate) {

}
