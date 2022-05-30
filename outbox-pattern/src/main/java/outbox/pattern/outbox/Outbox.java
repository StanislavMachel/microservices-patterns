package outbox.pattern.outbox;

import outbox.pattern.todo.KafkaTodoItemDto;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
public class Outbox {

    @Id
    private UUID          id;
    private String        operation;
    private String        aggregateId;
    private String        aggregateType;
    private String        message;
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

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregate) {
        this.aggregateType = aggregate;
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

    public static Outbox of(KafkaTodoItemDto kafkaTodoItemDto, Operation operation) {
        var outbox = new Outbox();
        outbox.setId(UUID.randomUUID());
        outbox.setAggregateId(kafkaTodoItemDto.getId().toString());
        outbox.setAggregateType(KafkaTodoItemDto.class.getName());
        outbox.setOperation(operation.name());
        outbox.setTs(ZonedDateTime.now());
        return outbox;
    }

    public static Outbox of(dev.machel.outbox.pattern.avro.schema.KafkaTodoItemDto kafkaTodoItemDto, Operation operation){
        var outbox = new Outbox();
        outbox.setId(UUID.randomUUID());
        outbox.setAggregateId(kafkaTodoItemDto.getId());
        outbox.setAggregateType(dev.machel.outbox.pattern.avro.schema.KafkaTodoItemDto.class.getName());
        outbox.setOperation(operation.name());
        outbox.setTs(ZonedDateTime.now());
        return outbox;
    }

}
