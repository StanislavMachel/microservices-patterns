package outbox.pattern.consumer;

import dev.machel.kafka.connect.avro.Outbox;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import outbox.pattern.consumer.configuration.KafkaConsumerConfig;

@Component
public class MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    @KafkaListener(topics = "${outbox.topic.name}", containerFactory = KafkaConsumerConfig.OUTBOX_KAFKA_LISTENER_CONTAINER_FACTORY_BEAN_NAME)
    public void outboxListener(ConsumerRecord<String, Outbox> message) {

        LOGGER.info("Received outbox message {}: {}", message.key(), message.value());
    }
}