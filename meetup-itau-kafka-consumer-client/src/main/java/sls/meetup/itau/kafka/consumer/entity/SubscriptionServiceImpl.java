package sls.meetup.itau.kafka.consumer.entity;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import sls.meetup.itau.kafka.consumer.common.PostMessageSendded;
import sls.meetup.itau.kafka.consumer.repository.StatusRepository;

@Service
public abstract class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    StatusRepository statusRepository;

    protected Long getStatus(String notificationType) {
        String statusString = StatusSubscription.forName(notificationType).orElseThrow(RuntimeException::new);
        Status status = this.statusRepository.findByName(statusString);
        return status.getId();
    }

    public void handleRecovery(ConsumerRecord<String, String> consumerRecord) {
        KafkaParameter parametroEnvioKafka = createKafkaParameter(consumerRecord);
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.sendDefault(parametroEnvioKafka.getKey(),
                parametroEnvioKafka.getValue());
        listenableFuture
                .addCallback(new PostMessageSendded(parametroEnvioKafka.getKey(), parametroEnvioKafka.getValue()));
    }

    private KafkaParameter createKafkaParameter(ConsumerRecord<String, String> consumerRecord) {
        return new KafkaParameter().builder().key(consumerRecord.key()).value(consumerRecord.value()).build();
    }
    
    public abstract String getName();
}
