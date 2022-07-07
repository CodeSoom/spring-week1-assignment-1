package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Task를 CRUD 로직을 처리하는 클래스입니다.
 */
public class TaskService {
    private Long id = 0L;
    private final List<Task> tasks = new ArrayList<>();

    /**
     * 받은 정보로 Task를 생성하여 저장하고 리턴한다.
     * @param title Task에 넣을 title
     * @return 생성한 Task 리턴
     */
    public Task createTask(String title) {
        Task task = toTask(title);
        tasks.add(task);
        return task;
    }

    /**
     * 입력 받은 데이터로 Task를 생성하고 id를 증가시키고 리턴한다.
     *
     * @param title Task에 넣을 title
     * @return 생성한 Task 리턴
     */
    private Task toTask(String title) {
        return new Task(id++, title);
    }
}
