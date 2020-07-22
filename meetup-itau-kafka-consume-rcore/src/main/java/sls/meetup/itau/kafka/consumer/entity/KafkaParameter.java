package sls.meetup.itau.kafka.consumer.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class KafkaParameter {

    private String key;
    private String value;

    public KafkaParameter withKey(String key) {
        this.key = key;
        return this;
    }

    public KafkaParameter withValue(SignatureDTO signature) {
        try {
            this.value = new ObjectMapper().registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule())
                    .writeValueAsString(signature);
        } catch (JsonProcessingException e) {
            log.error("Erro conversao de json {} ", e.getCause());
        }
        return this;
    }
}
