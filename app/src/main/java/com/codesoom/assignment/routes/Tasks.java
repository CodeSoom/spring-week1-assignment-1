package com.codesoom.assignment.routes;

import com.codesoom.assignment.errors.AlreadyExistsIDException;
import com.codesoom.assignment.errors.NotExistsIDException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Tasks {
    /**
     * When request GET method without id.
     *
     * @return JSON array string.
     * @throws JsonProcessingException internal server error.
     */
    public String get() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Task> tasks = TaskManager.find();
        return mapper.writeValueAsString(tasks);
    }

    /**
     * When request GET method with id.
     *
     * @param id is task id.
     * @return JSON string.
     * @throws NotExistsIDException when id is not exist.
     */
    public String get(long id) throws NotExistsIDException {
        Task task = TaskManager.find(id);
        return task.toString();
    }

    /**
     * When request POST method with id.
     *
     * @param task is want to insert task.
     * @throws AlreadyExistsIDException when task id is already exist.
     */
    public void post(Task task) throws AlreadyExistsIDException {
        if (task.id() == null) {
            TaskManager.insert(task.title());
        } else {
            TaskManager.insert(task);
        }
    }

    /**
     * When request PUT method with id.
     *
     * @param task is want to modify task.
     * @throws NotExistsIDException when task id is not exist.
     */
    public void put(Task task) throws NotExistsIDException {
        TaskManager.modify(task);
    }

    /**
     * When request PATCH method with id.
     *
     * @param id    is want to modify target task id.
     * @param title is want to modify task title.
     * @throws NotExistsIDException when task id is not exist.
     */
    public String patch(long id, String title) throws NotExistsIDException {
        Task task = TaskManager.modify(id, title);
        return task.toString();
    }

    /**
     * When request DELETE method with id.
     *
     * @param id is want to delete target task id.
     * @throws NotExistsIDException when task id is not exist.
     */
    public void delete(long id) throws NotExistsIDException {
        TaskManager.delete(id);
    }
}