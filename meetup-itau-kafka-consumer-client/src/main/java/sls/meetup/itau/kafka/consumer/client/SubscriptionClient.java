package sls.meetup.itau.kafka.consumer.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sls.meetup.itau.kafka.consumer.entity.Subscription;
import sls.meetup.itau.kafka.consumer.entity.SubscriptionService;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
@DependsOn({"subscriptionStrategy"})
public class SubscriptionClient {

    private final Map<String, SubscriptionService> subscriptionStrategy;

    @Autowired
    public SubscriptionClient(@Qualifier(value = "subscriptionStrategy") Map<String, SubscriptionService> subscriptionStrategy) {
        this.subscriptionStrategy = subscriptionStrategy;
    }

    @KafkaListener(topics = { "SUBSCRIPTION_PURCHASED", "SUBSCRIPTION_RESTARTED", "SUBSCRIPTION_CANCELED" })
    public void onMessage(ConsumerRecord<String, String> consumerRecord) throws IOException {
        log.info("ConsumerRecord : {} ", consumerRecord);
        Subscription singSubscriptionJson = createSignature(consumerRecord);
        this.subscriptionStrategy.get(singSubscriptionJson.getNotificationType()).processEvent(singSubscriptionJson);
    }

    private Subscription createSignature(ConsumerRecord<String, String> consumerRecord) throws IOException {
        return new ObjectMapper().readValue(consumerRecord.value(), Subscription.class);
    }
}
