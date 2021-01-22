package com.codesoom.assignment.routes;

import com.codesoom.assignment.errors.NotExistsIDException;
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
     * <p>상황 : Task 가 하나도 없을 때.</p>
     * <p>기대 : 비어있는 JSON array string 반환.</p>
     */
    @Test
    void getAllWithoutTasks() throws JsonProcessingException {
        String tasks = Tasks.get();

        assertEquals("[]", tasks);
    }

    /**
     * <p>메소드 : {@code Tasks.get}</p>
     * <p>상황 : Task 가 있을 때.</p>
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
     * <p>상황 : 입력된 ID의 Task 가 없을 때.</p>
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
     * <p>상황 : 입력된 ID의 Task 가 있을 때.</p>
     * <p>기대 : JSON string 반환.</p>
     */
    @Test
    void getOneWhenExist() {
        TaskManager.insert("sample");

        String task = Tasks.get(1);

        assertEquals("{\"id\":1,\"title\":\"sample\"}", task);
    }
}
