package outbox.pattern.todo;

import java.util.List;
import java.util.UUID;

public interface TodoService {

    TodoItem create(TodoItem todoItem);

    TodoItem update(UUID id, TodoItem todoItemDto);

    TodoItem findById(UUID id);

    List<TodoItem> findAll();

    void deleteById(UUID id);

}
