package outbox.pattern.todo;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.ZonedDateTime;

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

    @Test
    void create() {
    }

    @RepeatedTest(4)
    void update(RepetitionInfo repetitionInfo) {


        var todoItem = new TodoItem();
        todoItem.setName(String.format("Todo item %s created at %s", repetitionInfo.getCurrentRepetition(), ZonedDateTime.now()));

        this.restTemplate.postForEntity("http://localhost:" + port + "/api/v1/todos", todoItem, TodoItem.class);
    }

    @Test
    void delete() {
    }

}