package outbox.pattern.todo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoItemRepository todoItemRepository;

    public TodoServiceImpl(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    @Override
    public TodoItem create(TodoItem todoItem) {
        todoItem.setId(UUID.randomUUID());
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
