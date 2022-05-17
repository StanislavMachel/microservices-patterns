package outbox.pattern.todo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        Assertions.assertTrue(true); //TODO: write proper test
    }

    @Test
    void getAll() {
        Assertions.assertTrue(true); //TODO: write proper test
    }

    @RepeatedTest(1)
    void create(RepetitionInfo repetitionInfo) {
        var todoRequest = new TodoRequest();
        todoRequest.setName(String.format("Todo item %s created at %s", repetitionInfo.getCurrentRepetition(), ZonedDateTime.now()));
        var response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/v1/todos", todoRequest, TodoResponse.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
    }

    @RepeatedTest(1)
    void update(RepetitionInfo repetitionInfo) {
        var id = UUID.fromString("295295b7-1473-469f-b54b-01e88ebea8af");
        var todoRequest = new TodoRequest();
        todoRequest.setName(String.format("Todo item updated at %s", ZonedDateTime.now()));
        var responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/v1/todos/" + id, HttpMethod.PUT, new HttpEntity<>(todoRequest), TodoResponse.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @RepeatedTest(1)
    void delete(RepetitionInfo repetitionInfo) {
        UUID id = UUID.fromString("000881f3-66d2-4b0e-b5fb-872f3611162d");
        var responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/v1/todos/" + id, HttpMethod.DELETE, null, Void.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @RepeatedTest(10000)
    void testCreateUpdateDeleteFlow(RepetitionInfo repetitionInfo) {
        var createResponse = createNew(repetitionInfo.getCurrentRepetition());
        Assertions.assertNotNull(createResponse.getBody());
        Assertions.assertNotNull(createResponse.getBody().getId());

        UUID createdId = createResponse.getBody().getId();

        var getResponse = getExisting(createdId, TodoResponse.class);
        Assertions.assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Assertions.assertNotNull(getResponse.getBody());
        Assertions.assertNotNull(getResponse.getBody().getId());
        Assertions.assertEquals(createdId, getResponse.getBody().getId());
        Assertions.assertNotNull(getResponse.getBody().getName());
        Assertions.assertNotNull(getResponse.getBody().getCreated());
        Assertions.assertNotNull(getResponse.getBody().getUpdated());

        var updatedName = String.format("Todo item updated at %s", ZonedDateTime.now());
        var putResponse = updateExisting(createdId, updatedName, repetitionInfo.getCurrentRepetition());

        Assertions.assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        Assertions.assertNotNull(putResponse.getBody());
        Assertions.assertNotNull(putResponse.getBody().getName());
        Assertions.assertEquals(updatedName, putResponse.getBody().getName());

        var deleteResponse = deleteByIdRequest(createdId);
        Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        var getResponseAfterDelete = getExisting(createdId, String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getResponseAfterDelete.getStatusCode());
    }

    private ResponseEntity<TodoResponse> createNew(int currentRepetition) {
        var todoRequest = new TodoRequest();
        todoRequest.setName(String.format("Todo item %s created at %s", currentRepetition, ZonedDateTime.now()));
        var responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/api/v1/todos", todoRequest, TodoResponse.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        return responseEntity;
    }

    private ResponseEntity<TodoResponse> updateExisting(UUID id, String updatedName, int currentRepetition) {
        var todoRequest = new TodoRequest();
        todoRequest.setName(updatedName);

        var responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/v1/todos/" + id, HttpMethod.PUT, new HttpEntity<>(todoRequest), TodoResponse.class);
        return responseEntity;
    }

    private <T> ResponseEntity<T> getExisting(UUID id, Class<T> responseType){
        var responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/v1/todos/" + id, HttpMethod.GET, null, responseType);
        return responseEntity;
    }

    private ResponseEntity<Void> deleteByIdRequest(UUID id){
        var responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/v1/todos/" + id, HttpMethod.DELETE, null, Void.class);
        return responseEntity;
    }

}