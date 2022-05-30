package outbox.pattern.todo;

import java.util.List;
import java.util.UUID;

public interface TodoService {

    TodoResponse create(TodoRequest todoRequest);

    TodoResponse update(UUID id, TodoRequest todoRequest);

    TodoResponse findById(UUID id);

    List<TodoResponse> findAll();

    void deleteById(UUID id);

}
