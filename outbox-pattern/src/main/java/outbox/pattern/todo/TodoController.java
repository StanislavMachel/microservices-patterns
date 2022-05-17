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
    public TodoResponse getById(@PathVariable UUID id) {
        return todoService.findById(id);
    }

    @GetMapping
    public Iterable<TodoResponse> getAll() {
        return todoService.findAll();
    }

    @PostMapping
    public TodoResponse create(@RequestBody TodoRequest todoRequest) {
        return todoService.create(todoRequest);
    }

    @PutMapping("/{id}")
    public TodoResponse update(@PathVariable UUID id, @RequestBody TodoRequest todoRequest) {
        return todoService.update(id, todoRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        todoService.deleteById(id);
    }

}
