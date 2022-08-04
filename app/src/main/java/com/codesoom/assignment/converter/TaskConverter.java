package com.codesoom.assignment.converter;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskConverter {

    public static TaskConverter getInstance(){
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder{
        public static final TaskConverter INSTANCE = new TaskConverter();
    }

    public Task convert(String content) throws JsonProcessingException {
        Map<String , String> map = jsonToMap(content);
        Task task = new Task();
        Arrays.stream(getTaskDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            try {
                field.set(map.get(field.getName()) , task);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        System.out.println(task);
        return task;
//        return objectMapper.readValue(content , Task.class);
    }

    public String convert(Task task) throws IOException {
//        OutputStream outputStream = new ByteArrayOutputStream();
//        objectMapper.writeValue(outputStream , task);
        return "";
    }

    public String convert(Map<Long , Task> tasks) throws IOException {
//        OutputStream outputStream = new ByteArrayOutputStream();
//        objectMapper.writeValue(outputStream , new ArrayList<>(tasks.values()));
        return "";
    }

    public Field[] getTaskDeclaredFields(){
        return Task.class.getDeclaredFields();
    }

    public Map<String , String> jsonToMap(String content){
        Map<String , String> map = new HashMap<>();
        String[] contents = content.split(",");
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        for(String test : contents){
            Matcher matcher = pattern.matcher(test);
            String key = matcher.find() ? matcher.group().replace("\"" , "") : "";
            String value = matcher.find() ? matcher.group().replace("\"" , "") : "";
            map.put(key , value);
        }
        return map;
    }
}
