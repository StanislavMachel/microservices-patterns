package outbox.pattern.todo.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException(UUID id, Class<?> clazz) {
        super(String.format("Could not find %s with %s", clazz.getName(), id));
    }

}
