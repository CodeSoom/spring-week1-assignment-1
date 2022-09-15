package com.codesoom.assignment.repository;

import com.codesoom.assignment.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private Long seq = 1L;
    private final List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * 아이디를 기준으로 Task 목록에서 찾아 반환합니다.
     * @param id Task id
     * @return Task 객체
     */
    public Task getTaskById(Long id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }

    /**
     * Task 목록에 Task를 추가합니다.
     * @param newTask 추가할 새로운 Task
     */
    public void addTask(Task newTask) {
        newTask.setId(seq++);
        tasks.add(newTask);
    }

    /**
     * Task 목록에서 아이디로 Task를 찾아 수정합니다.
     * @param id 업데이트할 Task id
     * @param task 업데이트할 데이터
     * @return 업데이트된 Task
     */
    public Task updateTask(Long id, Task task) {
        Task foundTask = getTaskById(id);
        foundTask.setTitle(task.getTitle());
        return foundTask;
    }

    /**
     * Task 목록에서 해당 아이디를 갖는 Task를 삭제합니다.
     * @param id 삭제할 Task id
     * @return 삭제된 경우 true, 아니면 false
     */
    public boolean deleteTask(Long id) {
        return tasks.removeIf(task -> task.getId() == id);
    }
}
