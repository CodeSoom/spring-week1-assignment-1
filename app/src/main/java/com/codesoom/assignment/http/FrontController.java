package com.codesoom.assignment.http;

import com.codesoom.assignment.todolist.domain.Controller;
import com.codesoom.assignment.todolist.domain.PathVariable;
import com.codesoom.assignment.todolist.domain.RequestMapping;
import com.codesoom.assignment.todolist.domain.Task;
import com.codesoom.assignment.todolist.exceptions.NotFoundEntityException;
import com.codesoom.assignment.todolist.util.PathExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FrontController {
    private static final FrontController instance = new FrontController();
    private static final List<Controller> controllers = new ArrayList<>();
    private final ObjectMapper om;

    private FrontController() {
        om = new ObjectMapper();
    }

    public static FrontController getInstance() {
        return instance;
    }

    public void addController(Controller controller) {
        if (controllers.contains(controller)) {
            throw new RuntimeException();
        }
        controllers.add(controller);
    }

    public Response execute(HttpExchange exchange) throws IOException {
        final Controller findController = controllers.stream()
                .filter(controller -> controller.support(exchange.getRequestURI()))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        final Method availMethod = findController.getAvailMethod(exchange);
        final Object[] mappedParams = mappedParameter(availMethod, exchange);

        try {
            return (Response) availMethod.invoke(findController, mappedParams);
        } catch (InvocationTargetException e) {
            return handleInvocationException(e);
        } catch (NotFoundEntityException nfe) {
            return Response.from(HttpURLConnection.HTTP_NOT_FOUND);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Response handleInvocationException(InvocationTargetException e) {
        Throwable targetException = e.getTargetException();
        if (e.getTargetException().getClass().equals(NotFoundEntityException.class)) {
            return Response.from(HttpURLConnection.HTTP_NOT_FOUND);
        }
        return Response.of(HttpURLConnection.HTTP_INTERNAL_ERROR, targetException.getCause().toString());
    }

    private Object[] mappedParameter(Method method, HttpExchange exchange) throws IOException {
        final Parameter[] parameters = method.getParameters();
        Object[] mappedParams = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            mappedParams[i] = mapping(param, exchange, method);
        }

        return mappedParams;
    }

    private Object mapping(Parameter parameter, HttpExchange exchange, Method method) throws IOException {
        if (parameter.getParameterizedType().equals(HttpExchange.class)) {
            return exchange;
        }

        if (parameter.getParameterizedType().equals(Task.class)) {
            return om.readValue(exchange.getRequestBody(), Task.class);
        }

        if (parameter.isAnnotationPresent(PathVariable.class)) {
            Map<String, Object> pathValues = PathExtractor.extract(mixedPath(method),
                    exchange.getRequestURI().getPath());

            return toObject(parameter.getType(), String.valueOf(pathValues.get(parameter.getName())));
        }

        return parameter;
    }

    private String mixedPath(Method method) {
        return method.getDeclaringClass().getDeclaredAnnotation(RequestMapping.class).value()
                + method.getDeclaredAnnotation(RequestMapping.class).value();
    }

    public boolean support(URI uri) {
        return controllers.stream()
                .anyMatch(controller -> controller.support(uri));
    }

    public static Object toObject(Class clazz, String value) {
        if (Boolean.class == clazz) return Boolean.parseBoolean(value);
        if (Byte.class == clazz) return Byte.parseByte(value);
        if (Short.class == clazz) return Short.parseShort(value);
        if (Integer.class == clazz) return Integer.parseInt(value);
        if (Long.class == clazz) return Long.parseLong(value);
        if (Float.class == clazz) return Float.parseFloat(value);
        if (Double.class == clazz) return Double.parseDouble(value);
        return value;
    }


}
