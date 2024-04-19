package personal.finance.payment.application.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SubmitPaymentMethodRequest {
    public String token;
    public String userId;
}
