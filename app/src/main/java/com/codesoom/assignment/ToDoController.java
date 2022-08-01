package com.codesoom.assignment;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 할일을 추가, 조회, 수정, 삭제 하는 역할을 합니다
 */
public class ToDoController {
    private final ToDoRepository repository;
    private final AtomicLong lastId = new AtomicLong(1L);
    private final TaskMapper mapper = new TaskMapper();

    /**
     * @param repository Task를 보관하는 저장소
     */
    public ToDoController(ToDoRepository repository) {
        this.repository = repository;
    }

    /**
     * Id를 기반으로 Task를 조회합니다
     * @param taskId 조회할 task id
     * @return 조회된 Task
     * @throws TaskNotFoundException 저장된 Task가 없을 때 예외가 발생됩니다
     */
    public Task getTaskById(Long taskId) throws TaskNotFoundException {
        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isEmpty()) {
            throw new TaskNotFoundException();
        }
        return task.get();
    }

    /**
     * @return 저장된 Task 목록을 반환합니다
     */
    public List<Task> getTasks() {
        return repository.getTasks();
    }

    /**
     * @param body JSON String 형태의 Task
     * @return 새로 생성된 Task를 반환합니다.
     * @throws JsonProcessingException body로 입력받은 JSON String을 매핑하지 못할 떄 예외가 발생됩니다
     */
    public Task addTask(String body) throws JsonProcessingException {
        Task task = mapper.readTask(body);
        task.setId(lastId.getAndIncrement());
        repository.addTask(task);
        return task;
    }

    /**
     * @param taskId 변경할 Task의 Id
     * @param body 변경할 Task의 내용
     * @throws TaskNotFoundException 변경할 Task가 없을 때 예외가 발생합니다
     * @throws JsonProcessingException 변경할 Task의 내용을 매핑하지 못할 때 예외가 발생됩니다
     */
    public void updateTask(Long taskId, String body) throws TaskNotFoundException, JsonProcessingException {
        Task oldTask = getTaskById(taskId);
        Task newTask = mapper.readTask(body);
        oldTask.update(newTask);
    }

    /**
     * @param taskId 삭제할 Task의 Id
     * @throws TaskNotFoundException 삭제할 Task를 찾지 못할 때 예외가 발생됩니다
     */
    public void deleteTask(Long taskId) throws TaskNotFoundException {
        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isPresent()) {
            Task exitedTask = task.get();
            repository.deleteTask(exitedTask);
        } else {
            throw new TaskNotFoundException();
        }
    }
}
