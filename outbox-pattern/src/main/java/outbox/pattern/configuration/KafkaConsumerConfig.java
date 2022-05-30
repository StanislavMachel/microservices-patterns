package outbox.pattern.configuration;

import dev.machel.kafka.connect.avro.Outbox;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import outbox.pattern.todo.consumers.MessageListener;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    public static final  String OUTBOX_KAFKA_LISTENER_CONTAINER_FACTORY_BEAN_NAME = "outboxKafkaListenerContainerFactory";
    private static final String OUTBOX_CONSUMER_GROUP_ID                          = "kafka-consumer-outbox";

    @Value(value = "${kafka.host}")
    private String kafkaHost;

    @Bean
    public ConsumerFactory<String, ConsumerRecord<String, Outbox>> outboxConsumerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConsumerConfig.OUTBOX_CONSUMER_GROUP_ID);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        configs.put("schema.registry.url", "http://localhost:8081");
        configs.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, false);
        //configs.put()

        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean(name = KafkaConsumerConfig.OUTBOX_KAFKA_LISTENER_CONTAINER_FACTORY_BEAN_NAME)
    public ConcurrentKafkaListenerContainerFactory<String, ConsumerRecord<String, Outbox>> outboxKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ConsumerRecord<String, Outbox>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // factory.setMessageConverter(new CustomMessageConverter()); // <- possible write custom converter

        factory.setConsumerFactory(outboxConsumerFactory());

        factory.setErrorHandler((thrownException, data) -> {
            LOGGER.error("ErrorHandler: {}", thrownException.getMessage());
            LOGGER.info("Data: {}", data);
        });
        return factory;
    }

}
