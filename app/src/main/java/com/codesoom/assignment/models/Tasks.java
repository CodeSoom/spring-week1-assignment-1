package com.codesoom.assignment.models;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Tasks {
    private List<Task> tasks;

    public Tasks() {
        tasks = new ArrayList<>();
    }

    public String getAlltasksToJSON() {
        String content = "[";
        for(Task task : tasks) {
            content += task.toString() + ",";
        }
        if(content.contains(",")) {
            content = content.substring(0, content.length() - 1);
        }

        return content + "]";
    }

    public String getLatestTaskToJSON()  {
        return tasks.get(tasks.size() - 1).toString();
    }

    public String getTaskToJSON(int index)  {
        return Optional.ofNullable(tasks.get(index))
                .map(Task::toString)
                .orElseThrow(() -> new NoSuchElementException("해당 id는 tasks에 없습니다."));

        /*
        if(index >= tasks.size()) {
            throw new NoSuchElementException("해당 id는 tasks에 없습니다.")
        }
        return tasks.get(index).toString();
         */
    }

    public int getSize() {
        return tasks.size();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public void set(int index, Task task) {
        tasks.set(index, task);
    }

    public void remove(int index) {
        tasks.remove(index);
    }

}
