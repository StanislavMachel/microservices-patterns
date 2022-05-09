package outbox.pattern.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface TodoItemRepository extends JpaRepository<TodoItem, UUID> {
}
