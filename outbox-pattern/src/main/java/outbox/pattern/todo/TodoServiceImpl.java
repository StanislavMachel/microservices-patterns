package outbox.pattern.todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import outbox.pattern.outbox.Operation;
import outbox.pattern.outbox.Outbox;
import outbox.pattern.outbox.OutboxRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoItemRepository                      todoItemRepository;
    private final OutboxRepository                        outboxRepository;
    private final ObjectMapper                            objectMapper;
    private final KafkaTemplate<String, KafkaTodoItemDto> kafkaTemplate;

    public TodoServiceImpl(TodoItemRepository todoItemRepository,
                           OutboxRepository outboxRepository,
                           ObjectMapper objectMapper,
                           KafkaTemplate<String, KafkaTodoItemDto> kafkaTemplate) {
        this.todoItemRepository = todoItemRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public TodoItem create(TodoItem todoItem) {

        var now = ZonedDateTime.now();

        todoItem.setId(UUID.randomUUID());
        todoItem.setCreated(now);
        todoItem.setUpdated(now);
        var todo = todoItemRepository.save(todoItem);

        var kafkaTodoItem = KafkaTodoItemDto.of(todo);

        var outbox = new Outbox();
        outbox.setId(UUID.randomUUID());
        outbox.setAggregate(KafkaTodoItemDto.class.getName());
        outbox.setOperation(Operation.CREATE.name());
        outbox.setTs(now);

        try {
            outbox.setMessage(objectMapper.writeValueAsString(kafkaTodoItem));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        outboxRepository.save(outbox);

        //kafkaTemplate.send("todos", kafkaTodoItem);
        return todoItemRepository.save(todoItem);
    }

    @Override
    public TodoItem update(UUID id, TodoItem todoItem) {
        return todoItemRepository.findById(id)
                                 .map(todo -> {
                                     todo.setName(todoItem.getName());
                                     todoItemRepository.save(todoItem);
                                     return todo;
                                 })
                                 .orElseThrow(IllegalArgumentException::new);

    }

    @Override
    public TodoItem findById(UUID id) {
        return todoItemRepository.findById(id).orElseThrow(IllegalStateException::new);
    }

    @Override
    public List<TodoItem> findAll() {
        return todoItemRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        todoItemRepository.deleteById(id);
    }

}
