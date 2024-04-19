package personal.finance.payment.infrastracture.external;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StripeService {

    @Value("${stripe.api.key}")
    private String apiKey;

    public String getPriceId() {
        return priceId;
    }

    @Value("${stripe.price_id}")
    private String priceId;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public Customer createStripeCustomer(String email) throws Exception {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", email);
        return Customer.create(customerParams);
    }

    public Customer assignPaymentMethod(String customerId, String token) {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("source", token); // Associate the token with the customer
        try {
            Customer customer = Customer.retrieve(customerId);
            return customer.update(customerParams);
        } catch (StripeException stripeException) {
            log.info("Failed to assign payment method to a customer.");
            stripeException.printStackTrace();
            throw new IllegalStateException(stripeException.getMessage());
        }
    }

    public Subscription createSubscription(String customerId, String priceId) throws Exception {
        List<Object> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("price", priceId);
        items.add(item);

        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        params.put("items", items);
        return Subscription.create(params);
    }



}
