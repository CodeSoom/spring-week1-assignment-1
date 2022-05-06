package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.codesoom.assignment.models.TaskResult;

import java.util.ArrayList;
import java.util.List;

public enum HttpMethodProcessor {
    GET {
        @Override
        public TaskResult process(Task task) {
            List<Task> foundTask = new ArrayList<>();
            if (task.isEmpty()) {
                foundTask = taskManager.findTaskAll();
            } else {
                foundTask.add(taskManager.findTaskById(task.getId()));
            }

            return new TaskResult(foundTask, HttpStatus.OK);
        }
    },
    POST {
        @Override
        public TaskResult process(Task task) {
            List<Task> createdTask = new ArrayList<>();
            createdTask.add(taskManager.findTaskById(taskManager.insertTask(task)));
            return new TaskResult(createdTask, HttpStatus.CREATED);
        }
    },
    PUT {
        @Override
        public TaskResult process(Task task) {
            return updateProcess(task);
        }
    },
    PATCH {
        @Override
        public TaskResult process(Task task) {
            return updateProcess(task);
        }
    },
    DELETE {
        @Override
        public TaskResult process(Task task) {
            if (!isExist(task)) {
                return new TaskResult(null, HttpStatus.NOT_FOUND);
            }

            taskManager.deleteTask(task);
            return new TaskResult(null, HttpStatus.NO_CONTENT);
        }
    };

    private static final TaskManager taskManager = new TaskManager();
    public abstract TaskResult process(Task task);

    public TaskResult updateProcess(Task task) {
        if (!isExist(task)) {
            return new TaskResult(null, HttpStatus.NOT_FOUND);
        }

        List<Task> updatedTask = new ArrayList<>();
        updatedTask.add(taskManager.findTaskById(taskManager.updateTask(task)));
        return new TaskResult(updatedTask, HttpStatus.OK);
    }

    private static boolean isExist(Task task) {
        return !taskManager.findTaskById(task.getId()).isEmpty();
    }
}