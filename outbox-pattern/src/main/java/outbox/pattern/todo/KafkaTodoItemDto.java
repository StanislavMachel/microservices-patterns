package outbox.pattern.todo;

public class KafkaTodoItemDto {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "KafkaTodoItemDto{" +
                   "name='" + name + '\'' +
                   '}';
    }

}
