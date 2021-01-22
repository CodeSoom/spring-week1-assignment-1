package com.codesoom.assignment.web;

import com.codesoom.assignment.models.User;
import com.codesoom.assignment.service.UserService;
import com.codesoom.assignment.util.JsonUtil;
import com.codesoom.assignment.web.models.HttpRequestMethod;
import com.codesoom.assignment.web.models.HttpResponse;
import com.codesoom.assignment.web.models.HttpStatusCode;
import com.codesoom.assignment.web.service.RequestControllable;

public class UserHttpRequestContext extends HttpRequestContextBase {
    public static final String BASE_PATH = "/users";
    private final UserService userService;

    public UserHttpRequestContext(UserService userService) {
        this.userService = userService;
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
                String responseJson = JsonUtil.toJson(userService.getUsers());
                response = new HttpResponse(responseJson, HttpStatusCode.OK);
            } else {
                long id = parseIdFromPath(httpRequest.getPath());
                String responseJson = JsonUtil.toJson(userService.getUser(id));
                response = new HttpResponse(responseJson, HttpStatusCode.OK);
            }
            return response;
        };
    }

    private RequestControllable postTaskRequestController() {
        return httpRequest -> {
            User user = JsonUtil.toUser(httpRequest.getBody());
            User createdUser = userService.createNewUsers(user.getName(), user.getAge());
            String responseJson = JsonUtil.toJson(createdUser);
            return new HttpResponse(responseJson, HttpStatusCode.CREATED);
        };
    }

    private RequestControllable updateTaskRequestController() {
        return httpRequest -> {
            long id = parseIdFromPath(httpRequest.getPath());
            User user = JsonUtil.toUser(httpRequest.getBody());
            User updatedTask = userService.updateUser(id, user.getName(), user.getAge());
            String responseJson = JsonUtil.toJson(updatedTask);
            return new HttpResponse(responseJson, HttpStatusCode.OK);
        };
    }

    private RequestControllable deleteTaskRequestController() {
        return httpRequest ->  {
            long id = parseIdFromPath(httpRequest.getPath());
            userService.deleteUser(id);
            return new HttpResponse(HttpStatusCode.NO_CONTENT);
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
