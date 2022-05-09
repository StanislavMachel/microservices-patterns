package outbox.pattern.todo;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoItemRepository                      todoItemRepository;
    private final KafkaTemplate<String, KafkaTodoItemDto> kafkaTemplate;

    public TodoServiceImpl(TodoItemRepository todoItemRepository, KafkaTemplate<String, KafkaTodoItemDto> kafkaTemplate) {
        this.todoItemRepository = todoItemRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public TodoItem create(TodoItem todoItem) {
        todoItem.setId(UUID.randomUUID());


        var kafkaTodoItem = new KafkaTodoItemDto();
        kafkaTodoItem.setName(todoItem.getName());

        kafkaTemplate.send("todos", kafkaTodoItem);
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
