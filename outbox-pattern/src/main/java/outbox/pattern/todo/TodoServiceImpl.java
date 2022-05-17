package outbox.pattern.todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import outbox.pattern.outbox.Operation;
import outbox.pattern.outbox.Outbox;
import outbox.pattern.outbox.OutboxRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoItemRepository todoItemRepository;
    private final OutboxRepository   outboxRepository;
    private final ObjectMapper       objectMapper;

    public TodoServiceImpl(TodoItemRepository todoItemRepository,
                           OutboxRepository outboxRepository,
                           ObjectMapper objectMapper) {
        this.todoItemRepository = todoItemRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public TodoResponse create(TodoRequest todoRequest) {

        var todoItem = TodoItem.newTodoItemOf(todoRequest);

        var todo = todoItemRepository.save(todoItem);

        var kafkaTodoItem = KafkaTodoItemDto.of(todo);

        var outbox = Outbox.of(kafkaTodoItem, Operation.CREATE);
        outbox.setMessage(serialize(kafkaTodoItem));
        outboxRepository.save(outbox);

        var createdTodoItem = todoItemRepository.save(todoItem);

        return TodoResponse.of(createdTodoItem);
    }

    private String serialize(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public TodoResponse update(UUID id, TodoRequest todoRequest) {
        return todoItemRepository.findById(id)
                                 .map(todo -> {
                                     todo.setName(todoRequest.getName());
                                     todo.setUpdated(ZonedDateTime.now());

                                     KafkaTodoItemDto kafkaTodoItemDto = KafkaTodoItemDto.of(todo);

                                     Outbox outbox = Outbox.of(kafkaTodoItemDto, Operation.UPDATE);
                                     outbox.setMessage(serialize(kafkaTodoItemDto));
                                     outboxRepository.save(outbox);

                                     var savedTodoItem = todoItemRepository.save(todo);

                                     return TodoResponse.of(savedTodoItem);
                                 })
                                 .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public TodoResponse findById(UUID id) {
        return todoItemRepository.findById(id)
                                 .map(TodoResponse::of)
                                 .orElseThrow(IllegalStateException::new);
    }

    @Override
    public List<TodoResponse> findAll() {
        return todoItemRepository.findAll()
                   .stream()
                   .map(TodoResponse::of)
                   .collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void deleteById(UUID id) {

        KafkaTodoItemDto kafkaTodoItemDto = new KafkaTodoItemDto();
        kafkaTodoItemDto.setId(id);

        var outbox = Outbox.of(kafkaTodoItemDto, Operation.DELETE);
        outbox.setMessage(serialize(kafkaTodoItemDto));
        outboxRepository.save(outbox);

        todoItemRepository.deleteById(id);
    }

}
