package outbox.pattern.outbox;

import outbox.pattern.todo.KafkaTodoItemDto;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
public class Outbox {

    @Id
    private UUID   id;
    private String operation;
    private String aggregate;
    private String message;
    private ZonedDateTime ts;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAggregate() {
        return aggregate;
    }

    public void setAggregate(String aggregate) {
        this.aggregate = aggregate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getTs() {
        return ts;
    }

    public void setTs(ZonedDateTime ts) {
        this.ts = ts;
    }

    public static Outbox of(KafkaTodoItemDto kafkaTodoItemDto, Operation operation){
        var outbox = new Outbox();
        outbox.setId(UUID.randomUUID());
        outbox.setAggregate(KafkaTodoItemDto.class.getName());
        outbox.setOperation(operation.name());
        outbox.setTs(ZonedDateTime.now());
        return outbox;
    }



}
