package com.codesoom.assignment.web;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.util.JsonUtil;
import com.codesoom.assignment.web.models.HttpRequestMethod;
import com.codesoom.assignment.web.models.HttpResponse;
import com.codesoom.assignment.web.models.HttpStatusCode;
import com.codesoom.assignment.web.service.RequestControllable;

public class TaskHttpRequestContext extends HttpRequestContextBase {
    public static final String BASE_PATH = "/tasks";
    private final TaskService taskService;

    public TaskHttpRequestContext(TaskService taskService) {
        this.taskService = taskService;
        requestControllerMap.put(HttpRequestMethod.GET, getTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.POST, postTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.PUT, updateTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.PATCH, updateTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.DELETE, deleteTaskRequestController());
    }

    private RequestControllable getTaskRequestController() {
        return httpRequest -> {
            HttpResponse response;

            if (isRequestTaskList(httpRequest.getPath())) {
                String responseJson = JsonUtil.toJson(taskService.getTasks());
                response = new HttpResponse(responseJson, HttpStatusCode.OK);
            } else {
                long id = parseIdFromPath(httpRequest.getPath());
                String responseJson = JsonUtil.toJson(taskService.getTask(id));
                response = new HttpResponse(responseJson, HttpStatusCode.OK);
            }
            return response;
        };
    }

    private RequestControllable postTaskRequestController() {
        return httpRequest -> {
            Task task = JsonUtil.toTask(httpRequest.getBody());
            Task createdTask = taskService.createNewTask(task.getTitle());
            String responseJson = JsonUtil.toJson(createdTask);
            return new HttpResponse(responseJson, HttpStatusCode.CREATED);
        };
    }

    private RequestControllable updateTaskRequestController() {
        return httpRequest -> {
            long id = parseIdFromPath(httpRequest.getPath());
            Task task = JsonUtil.toTask(httpRequest.getBody());
            Task updatedTask = taskService.updateTask(id, task.getTitle());
            String responseJson = JsonUtil.toJson(updatedTask);
            return new HttpResponse(responseJson, HttpStatusCode.OK);
        };
    }

    private RequestControllable deleteTaskRequestController() {
        return httpRequest ->  {
            long id = parseIdFromPath(httpRequest.getPath());
            taskService.deleteTask(id);
            return new HttpResponse(HttpStatusCode.NO_CONTENT);
        };
    }

    private boolean isRequestTaskList(String path) {
        return path.equals("/tasks") || path.equals("/tasks/");
    }

    private long parseIdFromPath(String path) throws NumberFormatException {
        //path 마지막에 '/'이 붙어 있을 것을 대비
        String idString = path.replace("/tasks/", "").replace("/", "");
        return Long.parseLong(idString);
    }
}
