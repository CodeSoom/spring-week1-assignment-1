package com.codesoom.assignment.web.user;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.application.user.UserService;
import com.codesoom.assignment.web.*;

public class UserCollectionHttpRequestContext extends HttpRequestContextBase {
    private final UserService userService;

    public UserCollectionHttpRequestContext(String basePath, UserService userService) {
        super(basePath);
        this.userService = userService;
        requestControllerMap.put(HttpRequestMethod.GET, getTaskRequestController());
        requestControllerMap.put(HttpRequestMethod.POST, postTaskRequestController());
    }

    private RequestControllable getTaskRequestController() {
        return httpRequest -> {
            String responseJson = jsonMapper.toJson(userService.getUsers());
            return new HttpResponse(responseJson, HttpStatusCode.OK);
        };
    }

    private RequestControllable postTaskRequestController() {
        return httpRequest -> {
            User user = jsonMapper.toUser(httpRequest.getBody());
            User createdUser = userService.createNewUsers(user.getName(), user.getAge());
            String responseJson = jsonMapper.toJson(createdUser);
            return new HttpResponse(responseJson, HttpStatusCode.CREATED);
        };
    }

    @Override
    public boolean matchesPath(String path) {
        return path.equals(basePath) || path.equals(basePath + "/");
    }
}
