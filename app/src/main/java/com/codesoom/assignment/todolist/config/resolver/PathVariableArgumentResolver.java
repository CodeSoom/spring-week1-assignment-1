package com.codesoom.assignment.todolist.config.resolver;

import com.codesoom.assignment.common.MethodArgumentResolver;
import com.codesoom.assignment.todolist.domain.PathVariable;
import com.codesoom.assignment.todolist.domain.RequestMapping;
import com.codesoom.assignment.todolist.util.PathExtractor;
import com.sun.net.httpserver.HttpExchange;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class PathVariableArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supportParameter(Parameter parameter) {
        return parameter.isAnnotationPresent(PathVariable.class);
    }

    @Override
    public Object resolveArgument(HttpExchange exchange, Parameter parameter, Method method) {
        Map<String, Object> pathValues = PathExtractor.extract(mixedPath(method),
                exchange.getRequestURI().getPath());

        return toObject(parameter.getType(), String.valueOf(pathValues.get(parameter.getName())));
    }

    private String mixedPath(Method method) {
        return method.getDeclaringClass().getDeclaredAnnotation(RequestMapping.class).value()
                + method.getDeclaredAnnotation(RequestMapping.class).value();
    }

    public static Object toObject(Class<?> clazz, String value) {
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
