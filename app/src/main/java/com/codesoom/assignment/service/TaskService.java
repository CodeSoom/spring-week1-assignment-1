package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Task를 CRUD 로직을 처리하는 클래스입니다.
 */
public class TaskService {
    private Long id = 0L;
    private final List<Task> tasks = new CopyOnWriteArrayList<>();

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

    /**
     * 현재 가지고 있는 tasks를 리턴한다.
     *
     * @return 현재까지 저장된 tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * 요청 받은 숫자 타입 id에 맞는 task를 찾아 리턴한다.
     *
     * @param findId 요청 받은 숫자 타입의 id
     * @return 찾은 task를 리턴
     */
    public Optional<Task> getTask(Long findId) {
        return tasks.stream()
                .filter(t -> t.getId().equals(findId))
                .findFirst();
    }

    /**
     * 요청 받은 숫자 타입 id에 맞는 Task가 있으면 받은 title을 수정해서 리턴하고 없으면 null을 리턴합니다.
     *
     * @param findId 요청 받은 숫자 타입의 id
     * @param title Task의 변경할 title
     * @return 변경한 Task를 리턴, Task가 없다면 null 리턴
     */
    public Task changeTask(Long findId, String title) {
        Optional<Task> optionalTask = getTask(findId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(title);
            return task;
        }

        return null;
    }

    /**
     * 요청된 숫자 형식의 id와 같은 Task를 찾고 있으면 제거하고, 없으면 예외를 던집니다.
     * @param findId 요청된 숫자 형식의 id
     * @return 제거했다면 true, 아니라면 false 리턴
     */
    public boolean deleteTask(Long findId) {
        Optional<Task> optionalTask = getTask(findId);

        if (optionalTask.isPresent()) {
            tasks.remove(optionalTask.get());
            return true;
        }

        return false;
    }
}
