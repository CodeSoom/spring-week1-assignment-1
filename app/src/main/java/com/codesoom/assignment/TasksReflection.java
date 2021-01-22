package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpRequest;
import com.codesoom.assignment.models.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TasksReflection {

    public static <T> Object processMethod(Class<T> classType, HttpRequest httpRequest) {
        T instance = createInstance(classType);

        String path = httpRequest.getPath();
        RequestMethod requestMethod = httpRequest.getMethod();

        Object httpResponse = null;

        for (Method method : classType.getDeclaredMethods()) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);

            if (annotation != null && annotation.path().equals(path) && annotation.method().equals(requestMethod)) {
                try {
                    Object returnValue = method.invoke(instance, httpRequest);
                    httpResponse = returnValue;
                    return httpResponse;

                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }

            if (annotation != null && annotation.path().startsWith("/tasks/") && annotation.method().equals(requestMethod)) {
                try {
                    Object returnValue = method.invoke(instance, httpRequest);
                    httpResponse = returnValue;
                    return httpResponse;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return httpResponse;
    }

    private static <T> T createInstance(Class<T> classType) {
        try {
            return classType.getConstructor(null).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
