package sls.meetup.itau.kafka.consumer.component;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import sls.meetup.itau.kafka.consumer.common.PostMessageSendded;
import sls.meetup.itau.kafka.consumer.entity.KafkaParameter;
import sls.meetup.itau.kafka.consumer.entity.Messageria;
import sls.meetup.itau.kafka.consumer.entity.SignatureDTO;

import java.util.Arrays;
import java.util.List;

@Component
public class KafkaComponent implements Messageria {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public ListenableFuture<SendResult<String, String>> send(SignatureDTO signature) {
        KafkaParameter kafkaParameter = createKafkaParameter(signature);
        ProducerRecord<String, String> producerRecord = buildProducerRecord(kafkaParameter.getKey(),
                kafkaParameter.getValue(), signature.getNotificationType());
        ListenableFuture<SendResult<String, String>> listenableFuture = this.kafkaTemplate.send(producerRecord);
        listenableFuture
                .addCallback(new PostMessageSendded(kafkaParameter.getKey(), kafkaParameter.getValue()));
        return listenableFuture;
    }

    private KafkaParameter createKafkaParameter(SignatureDTO signature) {
        return new KafkaParameter().withKey(signature.getSubscription())
                .withValue(signature);
    }

    private ProducerRecord<String, String> buildProducerRecord(String key, String value, String topic) {
        List<Header> recordHeaders = Arrays.asList(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }
}
