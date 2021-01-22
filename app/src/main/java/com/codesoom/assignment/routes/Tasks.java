package com.codesoom.assignment.routes;

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
    public static String get() throws JsonProcessingException {
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
    public static String get(long id) throws NotExistsIDException {
        Task task = TaskManager.find(id);
        return task.toString();
    }

    /*public String post(Task task) {

    }

    public String put(Task task) {

    }

    public String patch(long id, String title) {

    }

    public void delete(long id) {

    }*/
}
