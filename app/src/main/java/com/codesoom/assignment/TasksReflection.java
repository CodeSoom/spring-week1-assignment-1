package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpRequest;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.models.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TasksReflection {

    private String pattern;
    private String annotationPattern;

    public TasksReflection(String path) {
        this.pattern = "^" + path + "/[0-9]*$";
        this.annotationPattern = "^" + path + "/\\{[a-zA-Z_$]+[a-zA-Z0-9_$]*\\}$";
    }

    public <T> HttpResponse processMethod(Class<T> classType, HttpRequest httpRequest) {
        T instance = createInstance(classType);

        String path = httpRequest.getPath();
        RequestMethod requestMethod = httpRequest.getMethod();

        for (Method method : classType.getDeclaredMethods()) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            if (requestMapping == null || !requestMapping.method().equals(requestMethod)) {
                continue;
            }

            if (requestMapping.path().equals(path)) {
                try {
                    return (HttpResponse) method.invoke(instance, httpRequest);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }

            if (requestMapping.path().matches(this.annotationPattern) && path.matches(this.pattern)) {
                try {
                    return (HttpResponse) method.invoke(instance, httpRequest);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return new HttpResponse(HttpStatus.NOT_FOUND, "");
    }

    private <T> T createInstance(Class<T> classType) {
        try {
            return classType.getConstructor((Class<?>[]) null).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
