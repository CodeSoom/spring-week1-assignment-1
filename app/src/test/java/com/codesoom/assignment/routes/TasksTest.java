package com.codesoom.assignment.routes;

import com.codesoom.assignment.errors.NotExistsIDException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TasksTest {
    @BeforeEach
    void beforeEach() {
        TaskManager.clear();
    }

    /**
     * <p>메소드 : {@code Tasks.get}</p>
     * <p>상황 : {@code Task}가 하나도 없을 때.</p>
     * <p>기대 : 비어있는 JSON array string 반환.</p>
     */
    @Test
    void getAllWithoutTasks() throws JsonProcessingException {
        String tasks = Tasks.get();

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

        String tasks = Tasks.get();

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
            Tasks.get(1);
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

        String task = Tasks.get(1);

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

        Tasks.post(new Task(null, title));

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

        Tasks.post(new Task(id, title));

        // 만약 id가 1인 Task 가 존재하지 않을 시 NotExistsIDException 을 던짐
        TaskManager.find(id);
    }
}
