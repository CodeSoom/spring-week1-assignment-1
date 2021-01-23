package com.codesoom.assignment.web.task;

import com.codesoom.assignment.application.task.TaskService;
import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.web.HttpRequestContextBase;
import com.codesoom.assignment.web.HttpRequestMethod;
import com.codesoom.assignment.web.HttpResponse;
import com.codesoom.assignment.web.HttpStatusCode;
import com.codesoom.assignment.web.RequestControllable;

public class TaskItemHttpRequestContext extends HttpRequestContextBase {
    private final TaskService taskService;

    public TaskItemHttpRequestContext(String basePath, TaskService taskService) {
        super(basePath);
        this.taskService = taskService;
        requestControllerMap.put(HttpRequestMethod.GET, getTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.PUT, updateTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.PATCH, updateTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.DELETE, deleteTaskRequestController());
    }

    private RequestControllable getTaskRequestController() {
        return httpRequest -> {
            long id = parseIdFromPath(httpRequest.getPath());
            String responseJson = jsonMapper.toJson(taskService.getTask(id));
            return new HttpResponse(responseJson, HttpStatusCode.OK);
        };
    }

    private RequestControllable updateTaskRequestController() {
        return httpRequest -> {
            long id = parseIdFromPath(httpRequest.getPath());
            Task task = jsonMapper.toTask(httpRequest.getBody());
            Task updatedTask = taskService.updateTask(id, task.getTitle());
            String responseJson = jsonMapper.toJson(updatedTask);
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

    @Override
    public boolean matchesPath(String path) {
        return path.startsWith(basePath) && !path.replace(basePath, "").isEmpty();
    }
}
