package com.codesoom.todo.service;

import com.codesoom.todo.models.Task;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TaskService {
    /*
         Q: 현재는 AtomiLong - Task 페어로 데이터를 저장합니다. 만약 이런 구조하면 실제로 사용되는 메모리 스페이스는
         1 : {id: 1, title: "test"} 의 형태로 저장이 됩니다. 이러면 데이터가 Task 라는 모델 안에 저장되지만,
         만약, AtomicLong - String 의 mapping 을 만들고 id - title 의 구조를 가져가고, 리스폰스를 줄때 둘을 json 형태로 가공해주는 방법이 있을 것 같은데
         DB 에서도 비슷한 상황이 발생 할것 같은데 어떤 기준으로 판단하는게 좋을까요?
     */
    private final ConcurrentHashMap<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong atomicID = new AtomicLong(0);

    @Nullable
    public Task getTask(Long id) {
        return this.tasks.get(id);
    }

    public ConcurrentHashMap<Long, Task> getTasks() {
        return this.tasks;
    }

    public Long addTask(Task newTasks) {
        newTasks.setId(atomicID.incrementAndGet());
        this.tasks.putIfAbsent(atomicID.get(), newTasks);
        return this.atomicID.get();
    }

    public void editTask(Task newTask) {
        Long id = newTask.getId();
        this.tasks.replace(id, newTask);
    }

    public void deleteTask(Long id) {
        this.tasks.remove(id);
    }
}
