package com.codesoom.assignment.web.user;

import com.codesoom.assignment.application.user.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.web.HttpRequestContextBase;
import com.codesoom.assignment.web.HttpRequestMethod;
import com.codesoom.assignment.web.HttpResponse;
import com.codesoom.assignment.web.HttpStatusCode;
import com.codesoom.assignment.web.RequestControllable;

public class UserItemHttpRequestContext extends HttpRequestContextBase {
    private final UserService userService;

    public UserItemHttpRequestContext(String basePath, UserService userService) {
        super(basePath);
        this.userService = userService;
        requestControllerMap.put(HttpRequestMethod.GET, getTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.PUT, updateTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.PATCH, updateTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.DELETE, deleteTaskRequestController());
    }

    private RequestControllable getTaskRequestController() {
        return httpRequest -> {
            long id = parseIdFromPath(httpRequest.getPath());
            String responseJson = jsonMapper.toJson(userService.getUser(id));
            return new HttpResponse(responseJson, HttpStatusCode.OK);
        };
    }

    private RequestControllable updateTaskRequestController() {
        return httpRequest -> {
            long id = parseIdFromPath(httpRequest.getPath());
            User user = jsonMapper.toUser(httpRequest.getBody());
            User updatedTask = userService.updateUser(id, user.getName(), user.getAge());
            String responseJson = jsonMapper.toJson(updatedTask);
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

    @Override
    public boolean matchesPath(String path) {
        return path.startsWith(basePath) && !path.replace(basePath, "").isEmpty();
    }
}
