package com.codesoom.assignment.web.task;

import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.application.task.TaskService;
import com.codesoom.assignment.web.*;

public class TaskCollectionHttpRequestContext extends HttpRequestContextBase {
    private final TaskService taskService;

    public TaskCollectionHttpRequestContext(String basePath, TaskService taskService) {
        super(basePath);
        this.taskService = taskService;
        requestControllerMap.put(HttpRequestMethod.GET, getTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.POST, postTaskRequestController());
    }

    private RequestControllable getTaskRequestController() {
        return httpRequest -> {
            String responseJson = jsonMapper.toJson(taskService.getTasks());
            return new HttpResponse(responseJson, HttpStatusCode.OK);
        };
    }

    private RequestControllable postTaskRequestController() {
        return httpRequest -> {
            Task task = jsonMapper.toTask(httpRequest.getBody());
            Task createdTask = taskService.createNewTask(task.getTitle());
            String responseJson = jsonMapper.toJson(createdTask);
            return new HttpResponse(responseJson, HttpStatusCode.CREATED);
        };
    }

    @Override
    public boolean matchesPath(String path) {
        return path.equals(basePath) || path.equals(basePath + "/");
    }
}
