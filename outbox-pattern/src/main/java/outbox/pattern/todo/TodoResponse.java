package outbox.pattern.todo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;
import java.util.UUID;

public class TodoResponse {

    private UUID id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime updated;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public static TodoResponse of(TodoItem todoItem) {
        var todoResponse = new TodoResponse();
        todoResponse.setId(todoItem.getId());
        todoResponse.setName(todoItem.getName());
        todoResponse.setCreated(todoItem.getCreated());
        todoResponse.setUpdated(todoItem.getUpdated());
        return todoResponse;
    }

    @Override
    public String toString() {
        return "TodoResponse{" +
                   "id=" + id +
                   ", name='" + name + '\'' +
                   ", created=" + created +
                   ", updated=" + updated +
                   '}';
    }

}
