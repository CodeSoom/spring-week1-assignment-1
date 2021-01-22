package com.codesoom.assignment.routes;

import com.codesoom.assignment.errors.AlreadyExistsIDException;
import com.codesoom.assignment.errors.NotExistsIDException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Tasks {
    private static final String PATH_SPLITTER = "/";
    private static final int ID_POSITION = 2;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Without body handler.
     *
     * @param method is request method.
     * @param path is request path.
     * @return response data.
     * @throws JsonProcessingException when parse request body failed.
     */
    public Response handler(String method, String path) throws JsonProcessingException {
        return handler(method, path, "");
    }

    /**
     * Task handler.
     *
     * @param method is request handler.
     * @param path is request path.
     * @param body is request body.
     * @return response data.
     * @throws JsonProcessingException when parse request body failed.
     */
    public Response handler(String method, String path, String body) throws JsonProcessingException {
        Long id = getIDFromPath(path);

        switch (method) {
            case "GET":
                try {
                    String content = id == null ? get() : get(id);
                    return new Response(Response.STATUS_OK, content);
                } catch (NotExistsIDException e) {
                    return new Response(Response.STATUS_NOT_FOUND, e.toString());
                }
            case "POST":
                Task task = mapper.readValue(body, Task.class);
                try {
                    post(task);
                    return new Response(Response.STATUS_CREATED, "");
                } catch (AlreadyExistsIDException e) {
                    return new Response(Response.STATUS_BAD_REQUEST, e.toString());
                }
            case "PUT":
                task = mapper.readValue(body, Task.class);
                try {
                    put(task);
                    return new Response(Response.STATUS_OK, body);
                } catch (NotExistsIDException e) {
                    return new Response(Response.STATUS_NOT_FOUND, e.toString());
                }
            case "PATCH":
                if (id == null) {
                    return new Response(Response.STATUS_BAD_REQUEST, "Not include id in path");
                }

                task = mapper.readValue(body, Task.class);
                try {
                    String data = patch(id, task.title());
                    return new Response(Response.STATUS_OK, data);
                } catch (NotExistsIDException e) {
                    return new Response(Response.STATUS_NOT_FOUND, e.toString());
                }
            case "DELETE":
                if (id == null) {
                    return new Response(Response.STATUS_BAD_REQUEST, "Not include id in path");
                }

                try {
                    delete(id);
                    return new Response(Response.STATUS_NO_CONTENT, "");
                } catch (NotExistsIDException e) {
                    return new Response(Response.STATUS_NOT_FOUND, e.toString());
                }
        }
        return new Response(Response.STATUS_NOT_FOUND, "Not found");
    }

    /**
     * Extract id from request path.
     *
     * @param path is request path.
     * @return Long type id. When not found id, returns null.
     */
    private Long getIDFromPath(String path) {
        String[] split = path.split(PATH_SPLITTER);
        if (split.length < ID_POSITION + 1) {
            return null;
        }
        return Long.parseLong(split[ID_POSITION]);
    }

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
