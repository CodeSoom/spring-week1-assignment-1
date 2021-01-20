package com.codesoom.assignment.models;

import com.codesoom.assignment.errors.AlreadyExistsIDException;
import com.codesoom.assignment.errors.NotExistsIDException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Task 의 관리를 위해 만들었습니다.
 */
public class TaskManager {
    private static long index = 0;
    private static final HashMap<Long, Task> TASKS = new HashMap<>();

    /**
     * findAll 의 역할을 합니다.
     * @return 전체 Task 를 List 로 반환합니다.
     */
    public static List<Task> find() {
        return new ArrayList<>(TASKS.values());
    }

    /**
     * findOne 의 역할을 합니다.
     * @param id 찾고자 하는 Task id
     * @return 존재 시 Task 반환, 없으면 null 반환
     */
    public static Task find(long id) {
        return TASKS.get(id);
    }

    /**
     * Task 를 받아서 insert 합니다. id가 존재시 Exception 을 반환합니다.
     * @param task 입력하고자 하는 Task 를 입력합니다.
     * @throws AlreadyExistsIDException 이미 id가 존재 할 경우
     */
    public static void insert(Task task) throws AlreadyExistsIDException {
        if (isExist(task.id())) {
            throw new AlreadyExistsIDException();
        }
        TASKS.put(task.id(), task);
    }

    /**
     * title 을 입력받아 Task 를 생성합니다.
     * @param title 입력하고자 하는 title 을 입력합니다.
     * @return id는 auto increase 하여 생성한 Task 를 반환합니다.
     */
    public static Task insert(String title) {
        index += 1;
        if (!isExist(index)) {
            Task task = new Task(index, title);
            TASKS.put(index, task);
            return task;
        }
        return insert(title);
    }

    /**
     * Task 를 입력받아 변경합니다.
     * @param task 변경할 Task 를 입력합니다.
     * @throws NotExistsIDException id가 존재하지 않을 시
     */
    public static void modify(Task task) throws NotExistsIDException {
        if (!isExist(task.id())) {
            throw new NotExistsIDException();
        }
        TASKS.replace(task.id(), task);
    }

    /**
     * id와 title 을 각각 입력받아 변경합니다.
     * @param id 대상 id
     * @param title 변경할 title
     * @return 변경된 Task 를 반환합니다.
     * @throws NotExistsIDException id가 존재하지 않을 시
     */
    public static Task modify(long id, String title) throws NotExistsIDException {
        if (!isExist(id)) {
            throw new NotExistsIDException();
        }
        Task task = new Task(id, title);

        TASKS.replace(task.id(), task);
        return task;
    }

    /**
     * Task 를 삭제합니다.
     * @param id 삭제할 Task id
     * @throws NotExistsIDException id가 존재하지 않을 시
     */
    public static void delete(long id) throws NotExistsIDException {
        if (!isExist(id)) {
            throw new NotExistsIDException();
        }
        TASKS.remove(id);
    }

    /**
     * id가 존재하는지 확인합니다.
     * @param id 확인하고자 하는 id
     * @return 존재 여부
     */
    private static boolean isExist(long id) {
        return TASKS.get(id) != null;
    }
}
