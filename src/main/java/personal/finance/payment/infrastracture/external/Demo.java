package personal.finance.payment.infrastracture.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class Demo {

    @Autowired
    ObjectMapper objectMapper;

    void stripe() {

    }


    Customer createCustomer() {
        Stripe.apiKey = "sk_test_51O5VVwHb4TtyICkwPx95GB0CsugVbKQ9vYDSMHLdT1y2bhM1HVJW0S2nmD0vPaSSIfkTYojY48pd9pkpgbuXpQyJ00hi099ela";
        CustomerCreateParams params =
            CustomerCreateParams.builder()
                .setName("Jenny Rosen")
                .setEmail("jennyrosen@example.com")
                .build();
        try {
            Customer customer = Customer.create(params);
            log.info("Customer created: " + objectMapper.writeValueAsString(customer));
            return customer;
        } catch (StripeException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    Product createProduct() {
        Stripe.apiKey = "sk_test_51O5VVwHb4TtyICkwPx95GB0CsugVbKQ9vYDSMHLdT1y2bhM1HVJW0S2nmD0vPaSSIfkTYojY48pd9pkpgbuXpQyJ00hi099ela";
        ProductCreateParams params =
            ProductCreateParams.builder().setName("Gold Plan").build();
        try {
            Product product = Product.create(params);
            log.info("product created: " + objectMapper.writeValueAsString(product));
            return product;
        } catch (StripeException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    Price createPrice(String productId) {
        Stripe.apiKey = "sk_test_51O5VVwHb4TtyICkwPx95GB0CsugVbKQ9vYDSMHLdT1y2bhM1HVJW0S2nmD0vPaSSIfkTYojY48pd9pkpgbuXpQyJ00hi099ela";
        PriceCreateParams params =
            PriceCreateParams.builder()
                .setCurrency("usd")
                .setUnitAmount(1000L)
                .setRecurring(
                    PriceCreateParams.Recurring.builder()
                        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                        .build()
                )
                .setProduct(productId)
                .build();
        try {
            Price price = Price.create(params);
            log.info("price created: " + objectMapper.writeValueAsString(price));
            return price;
        } catch (StripeException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }catch(JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    Subscription createSubscription(String customerId, String priceId) {
        Stripe.apiKey = "sk_test_51O5VVwHb4TtyICkwPx95GB0CsugVbKQ9vYDSMHLdT1y2bhM1HVJW0S2nmD0vPaSSIfkTYojY48pd9pkpgbuXpQyJ00hi099ela";
        SubscriptionCreateParams params =
            SubscriptionCreateParams.builder()
                .setCustomer("cus_Na6dX7aXxi11N4")
                .addItem(
                    SubscriptionCreateParams.Item.builder()
                        .setPrice("price_1MowQULkdIwHu7ixraBm864M")
                        .build()
                )
                .build();
        try {
            Subscription subscription = Subscription.create(params);
            log.info("subscription created: " + objectMapper.writeValueAsString(subscription));
            return subscription;
        } catch (StripeException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
