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
        this.id = ++Task.num;
    }

}
