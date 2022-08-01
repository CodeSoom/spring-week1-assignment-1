package com.codesoom.assignment.models;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Task를 삽입, 삭제, 검색할 수 있는 저장소입니다.
 */
public class TaskRepository {
    private final List<Task> tasks = new CopyOnWriteArrayList<>();

    /**
     * 요청 받은 숫자 형식의 id와 같은 Task를 리턴합니다.
     *
     * @param findId 요청 받은 숫자 타입의 id
     * @return Optional 타입의 Task 리턴
     */
    public Optional<Task> get(Long findId) {
        return tasks.stream()
                .filter(t -> t.getId().equals(findId))
                .findFirst();
    }

    /**
     * 요청 받은 Task를 저장소에 추가합니다.
     *
     * @param task 요청 받은 Task
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * 저장소에서 Task를 제거합니다.
     *
     * @param task 요청 받은 Task
     */
    public void remove(Task task) {
        tasks.remove(task);
    }
}
