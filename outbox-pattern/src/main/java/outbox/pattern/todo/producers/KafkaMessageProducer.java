package outbox.pattern.todo.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import outbox.pattern.todo.KafkaTodoItemDto;

@Component
public class KafkaMessageProducer implements MessageProducer {

    public static final String TODO_TOPIC = "todos";

    private final KafkaTemplate<String, KafkaTodoItemDto> kafkaTemplate;

    public KafkaMessageProducer(KafkaTemplate<String, KafkaTodoItemDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(KafkaTodoItemDto todoItemDto) {
        kafkaTemplate.send(TODO_TOPIC, todoItemDto);
    }

}
