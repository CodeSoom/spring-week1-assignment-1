package com.codesoom.assignment.task.repository;

import com.codesoom.assignment.storage.ListStorageRepositoryAbstract;
import com.codesoom.assignment.task.domain.Task;

public class TaskRepository extends ListStorageRepositoryAbstract<Task> {

    public Task update(Task task, Task source) {
        task.setTitle(source.getTitle());

        return task;
    }
}
