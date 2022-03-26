package com.codesoom.assignment.utils;

import com.codesoom.assignment.exception.IllegalAccessRuntimeException;
import com.codesoom.assignment.exception.NoDefaultConstructorException;
import com.codesoom.assignment.exception.NoSuchFieldRuntimeException;
import com.codesoom.assignment.exception.WrongJsonException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyObjectMapper {

    private<V> String getJsonPropertyString(String key, V value) {
        String keyString = wrapInDoubleQuotes(key);

        if(value instanceof Number) {
            return keyString + ":" + value;
        }

        return keyString + ":" + wrapInDoubleQuotes(value.toString());
    }

    public<T> String writeAsString(T object) throws IllegalAccessException {
        List<String> jsonProperties = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            // taskSequence 와 같은 static 필드가 JSON 문자열에 들어가지 않도록 함
            if(Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            jsonProperties.add(
                    getJsonPropertyString(field.getName(), field.get(object))
            );
        }

        return wrapInCurlyBrackets(String.join(",", jsonProperties));
    }

    public<T> String getJsonArray(Collection<T> collection) throws IllegalAccessException {
        List<String> jsonElement = new ArrayList<>();

        for (T t : collection) {
            jsonElement.add(writeAsString(t));
        }

        return wrapInBrackets(String.join(",", jsonElement));
    }

    protected String removeCurlyBrackets(String json) {
        String pattern = "\\{(.*)}";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(json);

        if(matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }

        throw new WrongJsonException("잘못된 JSON 데이터입니다.");
    }

    protected Map<String, String> getJsonPropertyMap(String json) {
        Map<String, String> resultMap = new HashMap<>();

        String pattern = "\"(.*?)\"";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(json);

        List<String> stringLiterals =  new ArrayList<>();

        while(matcher.find()) {
            String group = matcher.group(1);
            stringLiterals.add(group);
        }

        AtomicInteger groupIndex = new AtomicInteger();

        final String STRING_LITERAL = "STRING_LITERAL";
        String replacedJson = matcher.replaceAll(STRING_LITERAL);
        String[] jsonProperties = removeCurlyBrackets(replacedJson).split(",");

        for (String jsonProperty : jsonProperties) {
            List<String> keyValue = Arrays.stream(jsonProperty.split(":"))
                    .map((str) -> STRING_LITERAL.equals(str.trim()) ?
                            stringLiterals.get(groupIndex.getAndIncrement())
                            : str.trim()
                    ).toList();

            String key = keyValue.get(0);
            String value = keyValue.get(1);

            resultMap.put(key, value);
        }

        return resultMap;
    }

    protected <T> T getObject(Map<String, String> jsonPropertyMap, Class<T> clazz) {
        Constructor<T> constructor;

        try {
            constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new NoDefaultConstructorException("클래스의 기본 생성자가 존재하지 않습니다.");
        }

        T object;

        try {
            object = constructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new NoDefaultConstructorException("클래스의 기본 생성자를 호출할 수 없습니다.");
        }

        for (Map.Entry<String, String> entry : jsonPropertyMap.entrySet()) {
            try {
                Field field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(object, entry.getValue());
            } catch (NoSuchFieldException e) {
                throw new NoSuchFieldRuntimeException("필드명 [" + entry.getKey() +  "]는 해당 클래스에 존재하지 않습니다.");
            } catch (IllegalAccessException e) {
                throw new IllegalAccessRuntimeException("접근할 수 없는 필드에 접근하였습니다.");
            }
        }

        return object;
    }

    public <T> T readValue(String json, Class<T> clazz) {
        return getObject(getJsonPropertyMap(json), clazz);
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
