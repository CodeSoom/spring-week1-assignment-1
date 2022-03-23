package com.codesoom.assignment.domain;

/**
 * 할 일을 표현하는 클래스
 * ID 와 TITLE 을 통해 표현한다.
 */
public class Task {
    private static long ID_SERIAL_NUMBER = 1;

    private final Long id;
    private String title;

    // ObjectMapper 의 readValue() 메서드를 이용하기 위해 빈 생성자를 추가
    private Task() {
        this.id = ID_SERIAL_NUMBER++;
    }

    public Task(String title) {
        this.id = ID_SERIAL_NUMBER++;
        this.title = title;
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    // setters
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
