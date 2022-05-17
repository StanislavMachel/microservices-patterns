package outbox.pattern.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.ZonedDateTime;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getById() {
    }

    @Test
    void getAll() {
    }

    @RepeatedTest(1)
    void create(RepetitionInfo repetitionInfo) {
        var todoRequest = new TodoRequest();
        todoRequest.setName(String.format("Todo item %s created at %s", repetitionInfo.getCurrentRepetition(), ZonedDateTime.now()));
        var response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/v1/todos", todoRequest, TodoResponse.class);
    }

    @RepeatedTest(1)
    void update(RepetitionInfo repetitionInfo) {
        var id = UUID.fromString("0004fee0-65e4-4f5c-b8e1-440442c26c57");
        var todoRequest = new TodoRequest();
        todoRequest.setName(String.format("Todo item updated at %s", ZonedDateTime.now()));
        this.restTemplate.put("http://localhost:" + port + "/api/v1/todos/" + id, todoRequest);
    }

    @RepeatedTest(1)
    void delete() {
        UUID id = UUID.fromString("000a54f9-94c1-4f77-ad58-dbbd2225dfff");
        this.restTemplate.delete("http://localhost:" + port + "/api/v1/todos/" + id);
    }

}