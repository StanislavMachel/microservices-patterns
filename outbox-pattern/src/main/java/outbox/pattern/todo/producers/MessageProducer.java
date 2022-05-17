package outbox.pattern.todo.producers;

import outbox.pattern.todo.KafkaTodoItemDto;

public interface MessageProducer {
    void sendMessage(KafkaTodoItemDto todoItemDto);
}
