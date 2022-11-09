package com.codesoom.assignment.model;

public class Task {

    private static int num;
    private int id;
    private String title;

    public static int getNum() {
        return num;
    }

    public static void setNum(int num) {
        Task.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        //TODO 전후위 연산자 사용 없이 어떻게 값을 할당할 것인지?
        this.id = ++Task.num;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
