package com.codesoom.assignment;

import com.codesoom.assignment.controller.TasksController;
import com.codesoom.assignment.models.HttpRequest;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.models.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class TasksReflection {

    private final static String PATTERN = "^/tasks/[0-9]*$";
    private final static String ANNOTATION_PATTERN = "^/tasks/\\{[a-zA-Z_$]+[a-zA-Z0-9_$]*\\}$";

    private TasksController tasksController;

    public TasksReflection(TasksController tasksController) {
        this.tasksController = tasksController;
    }

    public HttpResponse processMethod(HttpRequest httpRequest) {
        Method[] methods = tasksController.getClass().getMethods();

        Optional<Method> matchedMethod = Arrays.stream(methods)
                .filter(method -> isMatched(method, httpRequest))
                .findFirst();

        if (!matchedMethod.isPresent()) {
            return new HttpResponse(HttpStatus.NOT_FOUND, "");
        }

        try {
            return (HttpResponse) matchedMethod.get().invoke(tasksController, httpRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isMatched(Method method, HttpRequest httpRequest) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        if(requestMapping == null || !isEqual(requestMapping, httpRequest)) {
            return false;
        }

        return true;
    }

    public boolean isEqual(RequestMapping requestMapping, HttpRequest httpRequest) {
        return isSameOrMatchedPath(requestMapping, httpRequest.getPath()) &&
                isSameMethod(requestMapping.method(), httpRequest.getMethod());
    }

    public boolean isSameOrMatchedPath(RequestMapping requestMapping, String path) {
        return isSamePath(requestMapping, path) || isMatchedPath(requestMapping, path);
    }

    public boolean isSameMethod(RequestMethod requestMappingMethod, RequestMethod requestMethod) {
        return requestMappingMethod == requestMethod;
    }

    public boolean isSamePath(RequestMapping requestMapping, String path) {
        return requestMapping.path().equals(path);
    }

    public boolean isMatchedPath(RequestMapping requestMapping, String path) {
        return requestMapping.path().matches(ANNOTATION_PATTERN) && path.matches(PATTERN);
    }

}
