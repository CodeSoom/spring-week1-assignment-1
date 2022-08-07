package com.codesoom.assignment.converter;

import com.codesoom.assignment.models.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskConverter {

    public static TaskConverter getInstance(){
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder{
        public static final TaskConverter INSTANCE = new TaskConverter();
    }

    public Task newTask(String content , Long taskId){
        Map<String , String> map = jsonToMap(content);
        String title = map.get("title");
        return new Task(taskId , title);
    }

    public String convert(Task task){
        return String.format("{\"id\":%d,\"title\":\"%s\"}" , task.getId() , task.getTitle());
    }

    public String convert(Map<Long , Task> tasks){
        StringBuilder sb = new StringBuilder();
        StringJoiner sj = new StringJoiner(",");
        sb.append("[");
        for(Task task : new ArrayList<>(tasks.values())){
            sj.add(convert(task));
        }
        sb.append(sj);
        sb.append("]");
        return sb.toString();
    }

    public Map<String , String> jsonToMap(String content){
        Map<String , String> map = new HashMap<>();
        String[] contents = content.split(",");
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        for(String test : contents){
            Matcher matcher = pattern.matcher(test);
            String key = getContent(matcher);
            String value = getContent(matcher);
            map.put(key , value);
        }
        return map;
    }

    public String getContent(Matcher matcher){
        if(!matcher.find())
            return "";
        return matcher.group().replace("\"" , "");
    }
}
