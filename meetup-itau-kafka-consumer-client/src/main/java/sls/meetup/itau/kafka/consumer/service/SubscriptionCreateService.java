package sls.meetup.itau.kafka.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Service;
import sls.meetup.itau.kafka.consumer.entity.Subscription;
import sls.meetup.itau.kafka.consumer.entity.SubscriptionServiceImpl;
import sls.meetup.itau.kafka.consumer.repository.SignatureRepository;

import java.io.IOException;
import java.time.LocalDate;

@Service
@Slf4j
public class SubscriptionCreateService extends SubscriptionServiceImpl {

    @Autowired
    SignatureRepository signatureRepository;

    static final String SUBSCRIPTION_PURCHASED = "SUBSCRIPTION_PURCHASED";

    @Override
    public void processEvent(Subscription subscriptionJson) throws IOException {
        log.info("subscription : {} ", subscriptionJson);
        saveSubscription(subscriptionJson);
        if (subscriptionJson.getSubscription() != null && subscriptionJson.getSubscription().equals("000")) {
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }
    }

    private Subscription saveSubscription(Subscription subscriptionJson) throws IOException {
        Subscription subscription = new Subscription().builder()
                .subscription(subscriptionJson.getSubscription())
                .notificationType(subscriptionJson.getNotificationType())
                .updatedAt(LocalDate.now())
                .createdAt(LocalDate.now())
                .statusId(getStatus(subscriptionJson.getNotificationType()))
                .build();
        Subscription subscriptionDB = signatureRepository.save(subscription);
        log.info("subscriptionDB : {} ", subscriptionDB);
        return signatureRepository.save(subscription);
    }

    @Override
    public String getName() {
        return SUBSCRIPTION_PURCHASED;
    }

}
