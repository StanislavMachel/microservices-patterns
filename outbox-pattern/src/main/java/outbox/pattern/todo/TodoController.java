package outbox.pattern.todo;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(TodoController.URL)
@RestController
public class TodoController {

    public static final String URL = "/api/v1/todos";

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/{id}")
    public TodoItem getById(@PathVariable UUID id) {
        return todoService.findById(id);
    }

    @GetMapping
    public Iterable<TodoItem> getAll() {
        return todoService.findAll();
    }

    @PostMapping
    public TodoItem create(@RequestBody TodoItem todoItem) {
        return todoService.create(todoItem);
    }

    @PutMapping("/{id}")
    public TodoItem update(@PathVariable UUID id, TodoItem todoItem) {
        return todoService.update(id, todoItem);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        todoService.deleteById(id);
    }

}
