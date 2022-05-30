package outbox.pattern.todo;

import dev.machel.outbox.pattern.avro.schema.KafkaTodoItemDto;

public class KafkaTodoItemUtils {

    private KafkaTodoItemUtils() {
        // utils class
    }

    public static KafkaTodoItemDto of(TodoItem todoItem) {
        var dto = new KafkaTodoItemDto();
        dto.setId(todoItem.getId().toString());
        dto.setName(todoItem.getName());
        dto.setCreated(todoItem.getCreated().toInstant());
        dto.setUpdated(todoItem.getUpdated().toInstant());
        return dto;
    }

}
