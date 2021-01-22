package com.codesoom.assignment.routes;

import com.codesoom.assignment.errors.AlreadyExistsIDException;
import com.codesoom.assignment.errors.NotExistsIDException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TasksTest {
    private final Tasks handler = new Tasks();

    @BeforeEach
    void beforeEach() {
        TaskManager.clear();
    }

    /**
     * <p>메소드 : {@code Tasks.handler}</p>
     * <p>상황 : {@code path}에 id가 없고, {@code Task}가 하나도 없을 때.</p>
     * <p>기대 : 비어있는 JSON array string 반환.</p>
     */
    @Test
    void handlerGetMethodWithoutIDWithoutTasks() throws JsonProcessingException {
        final String method = "GET";
        final String path = "/tasks/";

        Response response = handler.handler(method, path);
        assertEquals(Response.STATUS_OK, response.statusCode());
        assertEquals("[]", response.content());
    }

    /**
     * <p>메소드 : {@code Tasks.handler}</p>
     * <p>상황 : {@code path}에 id가 없고, {@code Task}가 있을 때.</p>
     * <p>기대 : JSON array string 반환.</p>
     */
    @Test
    void handlerGetMethodWithoutIDWithTasks() throws JsonProcessingException {
        final long id = 1L;
        final String title = "sample";
        TaskManager.insert(title);

        final String method = "GET";
        final String path = "/tasks/";

        Response response = handler.handler(method, path);
        assertEquals(Response.STATUS_OK, response.statusCode());
        assertEquals(String.format("[{\"id\":%d,\"title\":\"%s\"}]", id, title), response.content());
    }

    /**
     * <p>메소드 : {@code Tasks.handler}</p>
     * <p>상황 : {@code path}에 id가 있고, {@code Task}가 하나도 없을 때.</p>
     * <p>기대 : Status Not Found 를 반환.</p>
     */
    @Test
    void handlerGetMethodWithIDWithoutTasks() throws JsonProcessingException {
        final String method = "GET";
        final String path = "/tasks/1";

        Response response = handler.handler(method, path);
        assertEquals(Response.STATUS_NOT_FOUND, response.statusCode());
    }

    /**
     * <p>메소드 : {@code Tasks.handler}</p>
     * <p>상황 : {@code path}에 id가 있고, {@code Task}가 있을 때.</p>
     * <p>기대 : JSON string 반환.</p>
     */
    @Test
    void handlerGetMethodWithIDWithTasks() throws JsonProcessingException {
        final long id = 1L;
        final String title = "sample";
        TaskManager.insert(title);

        final String method = "GET";
        final String path = "/tasks/1";

        Response response = handler.handler(method, path);
        assertEquals(Response.STATUS_OK, response.statusCode());
        assertEquals(String.format("{\"id\":%d,\"title\":\"%s\"}", id, title), response.content());
    }

    /**
     * <p>메소드 : {@code Tasks.handler}</p>
     * <p>상황 : {@code method}가 POST 이고, {@code Task}에 id가 포함되어있지 않고, 중복되는 {@code Task}가 없을 때</p>
     * <p>기대 : Status Created 반환. {@code Task}가 자동 생성된 id를 가졌는지 확인.</p>
     */
    @Test
    void handlerPostMethodWithoutIDWithoutException() throws JsonProcessingException {
        final String title = "sample";
        final String method = "POST";
        final String path = "/tasks";
        final String body = String.format("{\"title\":\"%s\"}", title);

        Response response = handler.handler(method, path, body);
        assertEquals(Response.STATUS_CREATED, response.statusCode());

        // 최초 자동 생성 된 ID는 1이다.
        TaskManager.find(1);
    }

    /**
     * <p>메소드 : {@code Tasks.handler}</p>
     * <p>상황 : {@code method}가 POST 이고, {@code Task}에 id가 있고, 중복되는 {@code Task}가 없을 때</p>
     * <p>기대 : Status Created 반환. 입력된 {@code Task}가 존재하는지 확인.</p>
     */
    @Test
    void handlerPostMethodWithIDWithoutException() throws JsonProcessingException {
        final long id = 101L;
        final String title = "sample";
        final String method = "POST";
        final String path = "/tasks";
        final String body = String.format("{\"id\":%d,\"title\":\"%s\"}", id, title);

        Response response = handler.handler(method, path, body);
        assertEquals(Response.STATUS_CREATED, response.statusCode());

        TaskManager.find(id);
    }

    /**
     * <p>메소드 : {@code Tasks.handler}</p>
     * <p>상황 : {@code method}가 POST 이고, {@code Task}에 id가 있고, 중복되는 {@code Task}가 있을 때</p>
     * <p>기대 : Status Bad Request 반환.</p>
     */
    @Test
    void handlerPostMethodWithIDWithException() throws JsonProcessingException {
        final long id = 101L;
        final String title = "sample";
        final String method = "POST";
        final String path = "/tasks";
        final String body = String.format("{\"id\":%d,\"title\":\"%s\"}", id, title);

        TaskManager.insert(new Task(id, title));

        Response response = handler.handler(method, path, body);
        assertEquals(Response.STATUS_BAD_REQUEST, response.statusCode());
    }

    /**
     * <p>메소드 : {@code Tasks.get}</p>
     * <p>상황 : {@code Task}가 하나도 없을 때.</p>
     * <p>기대 : 비어있는 JSON array string 반환.</p>
     */
    @Test
    void getAllWithoutTasks() throws JsonProcessingException {
        String tasks = handler.get();

        assertEquals("[]", tasks);
    }

    /**
     * <p>메소드 : {@code Tasks.get}</p>
     * <p>상황 : {@code Task}가 있을 때.</p>
     * <p>기대 : JSON array string 반환.</p>
     */
    @Test
    void getAllWithTasks() throws JsonProcessingException {
        TaskManager.insert("sample");

        String tasks = handler.get();

        assertEquals("[{\"id\":1,\"title\":\"sample\"}]", tasks);
    }

    /**
     * <p>메소드 : {@code Tasks.get}</p>
     * <p>상황 : 입력된 ID의 {@code Task}가 없을 때.</p>
     * <p>기대 : {@code NotExistsIDException}를 던짐.</p>
     */
    @Test
    void getOneWhenNotExist() {
        try {
            handler.get(1);
        } catch (NotExistsIDException e) {
            return;
        }
        fail();
    }

    /**
     * <p>메소드 : {@code Tasks.get}</p>
     * <p>상황 : 입력된 ID의 {@code Task}가 있을 때.</p>
     * <p>기대 : JSON string 반환.</p>
     */
    @Test
    void getOneWhenExist() {
        TaskManager.insert("sample");

        String task = handler.get(1);

        assertEquals("{\"id\":1,\"title\":\"sample\"}", task);
    }

    /**
     * <p>메소드 : {@code Tasks.post}</p>
     * <p>상황 : 입력된 {@code Task}에 ID가 없을 때.</p>
     * <p>기대 : 자동입력된 ID가 들어간 {@code Task} 가 생성됨을 확인.</p>
     */
    @Test
    void postWithoutID() {
        final String title = "sample";

        handler.post(new Task(null, title));

        // 만약 id가 1인 Task 가 존재하지 않을 시 NotExistsIDException 을 던짐
        TaskManager.find(1);
    }

    /**
     * <p>메소드 : {@code Tasks.post}</p>
     * <p>상황 : 입력된 {@code Task}에 ID가 있을 때.</p>
     * <p>기대 : 입력된 {@code Task} 가 생성됨을 확인.</p>
     */
    @Test
    void postWithID() {
        final long id = 100L;
        final String title = "sample";

        handler.post(new Task(id, title));

        // 만약 id가 1인 Task 가 존재하지 않을 시 NotExistsIDException 을 던짐
        TaskManager.find(id);
    }

    /**
     * <p>메소드 : {@code Tasks.post}</p>
     * <p>상황 : 입력된 {@code Task}의 ID가 이미 있을 때.</p>
     * <p>기대 : {@code AlreadyExistsIDException}을 던짐.</p>
     */
    @Test
    void postWhenExistsID() {
        TaskManager.insert("sample");

        final long id = 1L;
        final String title = "sample";

        try {
            handler.post(new Task(id, title));
        } catch (AlreadyExistsIDException e) {
            return;
        }
        fail();
    }

    /**
     * <p>메소드 : {@code Tasks.put}</p>
     * <p>상황 : 입력된 {@code Task}의 ID가 존재할 때.</p>
     * <p>기대 : 에러가 나지 않음.</p>
     */
    @Test
    void putWhenExistID() {
        final long id = 1L;
        final String title = "sample";
        final String modifiedTitle = "modified sample";

        TaskManager.insert(new Task(id, title));
        handler.put(new Task(id, modifiedTitle));
    }

    /**
     * <p>메소드 : {@code Tasks.put}</p>
     * <p>상황 : 입력된 {@code Task}의 ID가 존재하지 않을 때.</p>
     * <p>기대 : {@code NotExistsIDException}을 던짐.</p>
     */
    @Test
    void putWhenNotExistID() {
        final long id = 1L;
        final String title = "sample";

        try {
            handler.put(new Task(id, title));
        } catch (NotExistsIDException e) {
            return;
        }
        fail();
    }

    /**
     * <p>메소드 : {@code Tasks.patch}</p>
     * <p>상황 : 입력된 {@code Task}의 ID가 존재할 때.</p>
     * <p>기대 : 변경된 {@code Task}의 JSON string 을 반환.</p>
     */
    @Test
    void patchWhenExistID() {
        final long id = 1L;
        final String title = "sample";
        final String modifiedTitle = "modified sample";

        TaskManager.insert(new Task(id, title));
        String data = handler.patch(id, modifiedTitle);

        assertEquals(String.format("{\"id\":%d,\"title\":\"%s\"}", id, modifiedTitle), data);
    }

    /**
     * <p>메소드 : {@code Tasks.patch}</p>
     * <p>상황 : 입력된 {@code Task}의 ID가 존재하지 않을 때.</p>
     * <p>기대 : {@code NotExistsIDException}을 던짐.</p>
     */
    @Test
    void patchWhenNotExistID() {
        final long id = 1L;
        final String title = "sample";

        try {
            handler.patch(id, title);
        } catch (NotExistsIDException e) {
            return;
        }
        fail();
    }

    /**
     * <p>메소드 : {@code Tasks.delete}</p>
     * <p>상황 : 입력된 ID가 존재할 때.</p>
     * <p>기대 : 에러를 던지지 않음.</p>
     */
    @Test
    void deleteWhenExistID() {
        final long id = 1L;
        final String title = "sample";

        TaskManager.insert(new Task(id, title));
        handler.delete(id);
    }

    /**
     * <p>메소드 : {@code Tasks.delete}</p>
     * <p>상황 : 입력된 ID가 존재하지 않을 때.</p>
     * <p>기대 : {@code NotExistsIDException}을 던짐.</p>
     */
    @Test
    void deleteWhenNotExistID() {
        final long id = 1L;

        try {
            handler.delete(id);
        } catch (NotExistsIDException e) {
            return;
        }
        fail();
    }
}
