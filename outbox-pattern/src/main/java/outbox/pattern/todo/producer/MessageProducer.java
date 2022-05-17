package outbox.pattern.todo.producer;

import outbox.pattern.todo.KafkaTodoItemDto;

public interface MessageProducer {
    void sendMessage(KafkaTodoItemDto todoItemDto);
}
