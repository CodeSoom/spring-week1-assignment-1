package com.codesoom.assignment.models;

import java.util.ArrayList;
import java.util.List;

public class Tasks {
    private List<Task> tasks;

    public Tasks() {
        tasks = new ArrayList<>();
    }

    public int getSize() {
        return tasks.size();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public void set(int index, Task task) {
        if(index >= tasks.size()) {
            throw new IndexOutOfBoundsException("입력한 id에는 Task가 없습니다." +
                    " 입력한 id의 숫자가 Tasks의 개수보다 큽니다." +
                    " 그래서 해당 id의 Task를 수정하지 못했습니다.");
        }
        tasks.set(index, task);
    }

    public void remove(int index) {
        if(index >= tasks.size()) {
            throw new IndexOutOfBoundsException("입력한 id에는 Task가 없습니다." +
                    " 입력한 id의 숫자가 Tasks의 개수보다 큽니다." +
                    " 그래서 해당 id의 Task를 지우지 못했습니다.");
        }
        tasks.remove(index);
    }

    public Task getTask(int index) {
        if(index >= tasks.size()) {
            throw new IndexOutOfBoundsException("입력한 id에는 Task가 없습니다." +
                    " 입력한 id의 숫자가 Tasks의 개수보다 큽니다." +
                    " 그래서 해당 id의 Task를 찾지 못했습니다.");
        }
        return tasks.get(index);
    }

}
