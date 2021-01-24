package com.codesoom.assignment.handler;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.ResponseHandlingException;
import com.codesoom.assignment.helper.TaskParser;
import com.codesoom.assignment.model.Task;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TaskHandler {
    TaskParser taskParser = new TaskParser();

    public String handleFetch(List<Task> tasks) throws IOException {
        return taskParser.tasksToJSON(tasks);
    }

    public String handleFetch(List<Task> tasks, Long taskId) throws IOException, ResponseHandlingException {
        Task selectedTask = taskParser.getTask(tasks, taskId);

        if (selectedTask.getId() == null) {
            throw new ResponseHandlingException(HttpStatusCode.NOT_FOUND);
        }

        return taskParser.taskToJSON(selectedTask);
    }

    public String handleAdd(List<Task> tasks, String title) throws IOException {
        Task newTask = taskParser.toTask(title, (long) (tasks.size() + 1));
        tasks.add(newTask);
        return taskParser.taskToJSON(tasks.get(tasks.size() - 1));
    }

    public String handleUpdate(List<Task> tasks, Long taskId, String title) throws IOException, ResponseHandlingException {
        Task editableTask = taskParser.getTask(tasks, taskId);

        if (editableTask.getId() == null) {
            throw new ResponseHandlingException(HttpStatusCode.NOT_FOUND);
        }

        tasks.set(tasks.indexOf(editableTask), taskParser.toTask(title, editableTask.getId()));
        return taskParser.taskToJSON(taskParser.getTask(tasks, taskId));
    }

    public String handleRemove(List<Task> tasks, Long taskId) throws ResponseHandlingException {
        if (taskParser.getTask(tasks, taskId).getId() == null) {
            throw new ResponseHandlingException(HttpStatusCode.NOT_FOUND);
        }

        tasks.removeIf(task -> Objects.equals(taskId, task.getId()));
        return "";
    }
}
