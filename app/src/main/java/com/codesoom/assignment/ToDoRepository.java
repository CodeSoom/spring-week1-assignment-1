package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Task를 저장하는 객체 입니다
 */
public class ToDoRepository {
    private final List<Task> tasks = Collections.synchronizedList(new ArrayList<>());

    /**
     * Task Id로 Task를 조회합니다
     * @param taskId Task Id
     * @return 조회된 Task
     */
    public Optional<Task> getTaskById(Long taskId) {
        return tasks
                .stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst();
    }

    /**
     * Task를 저장소에 추가합니다
     * @param task 저장소에 추가할 task
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Task를 저장소에서 제거합니다
     * @param targetTask 저장소에서 제거할 task
     */
    public void deleteTask(Task targetTask) {
        tasks.removeIf(task -> task.getId().equals(targetTask.getId()));
    }

    /**
     * Task 목록을 가져옵니다
     * @return Task 목록
     */
    public List<Task> getTasks() {
        return tasks;
    }

}
