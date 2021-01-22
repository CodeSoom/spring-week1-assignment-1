package com.codesoom.assignment.web.task;

import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.application.task.TaskService;
import com.codesoom.assignment.application.JsonUtil;
import com.codesoom.assignment.web.*;

import java.util.Optional;

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
            return taskService.deleteTask(id)
                    .map(e ->  new HttpResponse(HttpStatusCode.NO_CONTENT))
                    .or(() -> Optional.of(new HttpResponse(HttpStatusCode.NO_CONTENT)));
        };
    }

    private boolean isRequestTaskList(String path) {
        return path.equals(BASE_PATH) || path.equals(BASE_PATH + "/");
    }

    private long parseIdFromPath(String path) throws NumberFormatException {
        //path 마지막에 '/'이 붙어 있을 것을 대비
        String idString = path.replace(BASE_PATH, "").replaceAll("/", "");
        return Long.parseLong(idString);
    }
}
