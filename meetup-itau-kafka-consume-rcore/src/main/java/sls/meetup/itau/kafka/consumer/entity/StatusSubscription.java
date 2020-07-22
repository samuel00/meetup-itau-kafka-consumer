package sls.meetup.itau.kafka.consumer.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum StatusSubscription {

    SUBSCRIPTION_PURCHASED ("SUBSCRIPTION_PURCHASED", "CREATE"),
    SUBSCRIPTION_RESTARTED ("SUBSCRIPTION_RESTARTED", "UPDATE"),
    SUBSCRIPTION_CANCELED ("SUBSCRIPTION_CANCELED", "CANCEL");

    private String key;
    private String value;

    StatusSubscription(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private static final Map<String, StatusSubscription> nameToValueMap =  new HashMap<String, StatusSubscription>();

    static {
        for (StatusSubscription value : EnumSet.allOf(StatusSubscription.class)) {
            nameToValueMap.put(value.name(), value);
        }
    }

    public static Optional<String> forName(String name) {
        return Optional.ofNullable(nameToValueMap.get(name).value);
    }
}
