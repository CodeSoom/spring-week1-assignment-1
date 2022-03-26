package com.codesoom.assignment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyObjectMapper {

    private<V> String getJsonPropertyString(String key, V value) {
        String keyString = wrapInDoubleQuotes(key);

        if(value instanceof Number) {
            return keyString + ":" + value;
        }

        return keyString + ":" + wrapInDoubleQuotes(value.toString());
    }

    public<T> String getJson(T object) throws IllegalAccessException {
        List<String> jsonProperties = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            jsonProperties.add(
                    getJsonPropertyString(field.getName(), field.get(object))
            );
        }

        return wrapInCurlyBrackets(String.join(",", jsonProperties));
    }

    public<T> String getJsonArray(Collection<T> collection) throws IllegalAccessException {
        List<String> jsonElement = new ArrayList<>();

        for (T t : collection) {
            jsonElement.add(getJson(t));
        }

        return wrapInBrackets(String.join(",", jsonElement));
    }

    private String wrapInDoubleQuotes(String string) {
        return "\"" + string + "\"";
    }

    private String wrapInCurlyBrackets(String string) {
        return "{" + string + "}";
    }

    private String wrapInBrackets(String string) {
        return "[" + string + "]";
    }
}
