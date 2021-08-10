package com.codesoom.assignment.http;

import com.codesoom.assignment.common.MethodArgumentResolver;
import com.codesoom.assignment.todolist.domain.Controller;
import com.codesoom.assignment.todolist.exceptions.AlreadyExistsControllerException;
import com.codesoom.assignment.todolist.exceptions.AlreadyExistsResolverException;
import com.codesoom.assignment.todolist.exceptions.NotFoundSupportedResolverException;
import com.codesoom.assignment.todolist.exceptions.NotFoundEntityException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FrontController {
    private static final FrontController instance = new FrontController();

    private static final List<Controller> controllers = new ArrayList<>();
    private static final List<MethodArgumentResolver> argumentResolvers = new ArrayList<>();

    private FrontController() {}

    public static FrontController getInstance() {
        return instance;
    }

    public void addController(Controller controller) {
        if (controllers.contains(controller)) {
            throw new AlreadyExistsControllerException();
        }
        controllers.add(controller);
    }

    public void addArgumentResolver(List<MethodArgumentResolver> resolvers) {
        resolvers.forEach(this::addArgumentResolver);
    }

    public void addArgumentResolver(MethodArgumentResolver resolver) {
        if (argumentResolvers.contains(resolver)) {
            throw new AlreadyExistsResolverException();
        }
        argumentResolvers.add(resolver);
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

    private Object[] mappedParameter(Method method, HttpExchange exchange) {
        final Parameter[] parameters = method.getParameters();
        Object[] mappedParams = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            mappedParams[i] = mapping(param, exchange, method);
        }

        return mappedParams;
    }

    private Object mapping(Parameter parameter, HttpExchange exchange, Method method) {
        MethodArgumentResolver supportedResolver = argumentResolvers.stream()
                .filter(resolver -> resolver.supportParameter(parameter))
                .findAny()
                .orElseThrow(NotFoundSupportedResolverException::new);

        return supportedResolver.resolveArgument(exchange, parameter, method);
    }

    public boolean support(URI uri) {
        return controllers.stream()
                .anyMatch(controller -> controller.support(uri));
    }
}
