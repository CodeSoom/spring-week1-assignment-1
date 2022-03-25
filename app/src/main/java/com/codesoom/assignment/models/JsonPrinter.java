package com.codesoom.assignment.models;

import java.util.StringTokenizer;

public class JsonPrinter {
    public static String printTask(Task task) {
        StringTokenizer tokens = new StringTokenizer(task.toString(), task.getDelimiter());
        return "{\"id\":" + tokens.nextToken() +
                ",\"title\":" + "\"" + tokens.nextToken() + "\"" + "}";
    }

    public static String printAllTasks(Tasks tasks) {
        String jsonContent = "\"tasks\":[";

        for(int i = 0; i < tasks.getSize(); i++) {
            jsonContent += printTask(tasks.getTask(i)) + ",";
        }

        if(jsonContent.contains(",")) {
            jsonContent = jsonContent.substring(0, jsonContent.length() - 1);
        }

        return jsonContent + "]";
    }

}
