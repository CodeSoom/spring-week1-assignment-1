package com.codesoom.assignment.web.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.application.user.UserService;
import com.codesoom.assignment.web.*;

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
                String responseJson = jsonMapper.toJson(userService.getUsers());
                response = new HttpResponse(responseJson, HttpStatusCode.OK);
            } else {
                long id = parseIdFromPath(httpRequest.getPath());
                String responseJson = jsonMapper.toJson(userService.getUser(id));
                response = new HttpResponse(responseJson, HttpStatusCode.OK);
            }
            return response;
        };
    }

    private boolean isRequestTaskList(String path) {
        return path.equals(BASE_PATH) || path.equals(BASE_PATH + "/");
    }

    private RequestControllable postTaskRequestController() {
        return httpRequest -> {
            User user = jsonMapper.toUser(httpRequest.getBody());
            User createdUser = userService.createNewUsers(user.getName(), user.getAge());
            String responseJson = jsonMapper.toJson(createdUser);
            return new HttpResponse(responseJson, HttpStatusCode.CREATED);
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

    private long parseIdFromPath(String path) throws NumberFormatException {
        //path 마지막에 '/'이 붙어 있을 것을 대비
        String idString = path.replace(BASE_PATH, "").replaceAll("/", "");
        return Long.parseLong(idString);
    }
}
