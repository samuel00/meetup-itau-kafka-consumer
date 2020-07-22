package sls.meetup.itau.kafka.consumer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sls.meetup.itau.kafka.consumer.entity.SubscriptionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class Strategy {

    @Bean(name = "subscriptionStrategy")
    Map<String, SubscriptionService> subscriptionStrategy(List<SubscriptionService> subscriptionServices) {
        Map<String, SubscriptionService> map = new HashMap<>();
        subscriptionServices.forEach(subscriptionService -> map.put(subscriptionService.getName(), subscriptionService));
        return map;
    }
}
