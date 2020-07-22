package sls.meetup.itau.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import sls.meetup.itau.kafka.consumer.client.SubscriptionClient;
import sls.meetup.itau.kafka.consumer.entity.Subscription;
import sls.meetup.itau.kafka.consumer.entity.SubscriptionService;
import sls.meetup.itau.kafka.consumer.service.SubscriptionCreateService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = ApplicationStarter.class )
@EmbeddedKafka(topics = {"SUBSCRIPTION_RESTARTED"})
@TestPropertySource(properties = { "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}" })
@ActiveProfiles("test")
public class ClientKafkaTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    KafkaListenerEndpointRegistry endpointRegistry;

    @SpyBean
    private Map<String, SubscriptionService> subscriptionStrategy;

    @SpyBean
    SubscriptionClient subscriptionClient;

    @BeforeEach
    void setup() {
        this.endpointRegistry.getListenerContainers().forEach(messageListenerContainer ->
                ContainerTestUtils.waitForAssignment(messageListenerContainer,6));
    }

    @Test
    void publicaNovaTransferencia() throws InterruptedException, IOException {
        //given
        String json = "{\"notificationType\" : \"SUBSCRIPTION_RESTARTED\", \"subscription\" : \"123456789\"}";

        //when
        kafkaTemplate.send("SUBSCRIPTION_RESTARTED", json);
        new CountDownLatch(1).await(3, TimeUnit.SECONDS);

        //then
        verify(subscriptionClient, times(1)).onMessage(isA(ConsumerRecord.class));

    }
}
