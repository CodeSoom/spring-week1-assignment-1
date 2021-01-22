package com.codesoom.assignment.models;

import com.codesoom.assignment.errors.AlreadyExistsIDException;
import com.codesoom.assignment.errors.NotExistsIDException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    @BeforeEach
    void beforeEach() {
        TaskManager.clear();
    }

    /**
     * <p>메소드 : {@code TaskManager.find}</p>
     * <p>상황 : 존재하지 않는 ID를 조회.</p>
     * <p>기대 : {@code NotExistsIDException} 을 던짐.</p>
     */
    @Test
    void findNotExistID() {
        try {
            TaskManager.find(100);
        } catch (NotExistsIDException ignored) {
            return;
        }
        fail();
    }

    /**
     * <p>메소드 : {@code TaskManager.insert}</p>
     * <p>상황 : 변수를 입력해 insert.</p>
     * <p>기대 : 자동 생성된 ID를 가진 Task 가 만들어짐.</p>
     */
    @Test
    void insertTaskByVariables() {
        final long id = 1;
        final String title = "sample";

        Task task = TaskManager.insert(title);

        assertEquals(title, task.title());
        assertEquals(id, task.id());
    }

    /**
     * <p>메소드 : {@code TaskManager.insert}</p>
     * <p>상황 : Task Object 를 입력해 insert.</p>
     * <p>기대 : 입력된 Task Object 가 그대로 입력됨을 확인.</p>
     */
    @Test
    void insertTaskByTaskObject() {
        final long id = 2;
        final String title = "sample2";

        TaskManager.insert(new Task(id, title));
        Task task = TaskManager.find(id);

        assertEquals(title, task.title());
        assertEquals(id, task.id());
    }

    /**
     * <p>메소드 : {@code TaskManager.insert}</p>
     * <p>상황 : 이미 존재하는 ID를 가진 Task Object 를 insert.</p>
     * <p>기대 : {@code AlreadyExistsIDException}를 던짐.</p>
     */
    @Test
    void insertAlreadyExistID() {
        // insert task. id: 1, title: "sample".
        TaskManager.insert("sample");

        try {
            TaskManager.insert(new Task(1, "sample"));
        } catch (AlreadyExistsIDException ignored) {
            return;
        }
        fail();
    }

    /**
     * <p>메소드 : {@code TaskManager.modify}</p>
     * <p>상황 : Task Object 를 입력받아 기존의 Task 를 modify.</p>
     * <p>기대 : 성공적으로 수정됨.</p>
     */
    @Test
    void modifyTaskByTaskObject() {
        // insert task. id: 1, title: "sample".
        TaskManager.insert("sample");

        final long id = 1;
        final String title = "modified sample";

        TaskManager.modify(new Task(id, title));

        Task task = TaskManager.find(id);

        assertEquals(title, task.title());
    }

    /**
     * <p>메소드 : {@code TaskManager.modify}</p>
     * <p>상황 : 각각의 변수를 입력받아 modify.</p>
     * <p>기대 : 성공적으로 수정됨.</p>
     */
    @Test
    void modifyTaskByVariables() {
        // insert task. id: 1, title: "sample".
        TaskManager.insert("sample");

        final long id = 1;
        final String title = "modified sample";

        Task task = TaskManager.modify(id, title);

        assertEquals(title, task.title());
    }

    /**
     * <p>메소드 : {@code TaskManager.modify}</p>
     * <p>상황 : 존재하지 않는 ID의 Task 를 modify.</p>
     * <p>기대 : {@code NotExistsIDException}를 뱉음.</p>
     */
    @Test
    void modifyNotExistID() {
        try {
            TaskManager.modify(new Task(100, "not exist"));
        } catch (NotExistsIDException ignored) {
            return;
        }
        fail();
    }

    /**
     * <p>메소드 : {@code TaskManager.delete}</p>
     * <p>상황 : 존재하는 ID를 delete.</p>
     * <p>기대 : 삭제된 ID를 조회하려 하면 {@code NotExistsIDException}를 뱉음.</p>
     */
    @Test
    void deleteTask() {
        // insert task. id: 1, title: "sample".
        TaskManager.insert("sample");

        final long id = 1;
        TaskManager.delete(id);

        try {
            TaskManager.find(id);
        } catch (NotExistsIDException ignored) {
            return;
        }
        fail();
    }

    /**
     * <p>메소드 : {@code TaskManager.delete}</p>
     * <p>상황 : 존재하지 않는 ID를 delete.</p>
     * <p>기대 : {@code NotExistsIDException}를 뱉음.</p>
     */
    @Test
    void deleteNotExistID() {
        try {
            TaskManager.delete(100);
        } catch (NotExistsIDException ignored) {
            return;
        }
        fail();
    }
}
