package com.codesoom.assignment.utils;

public class IdGenerator {

    private Long id = 0L;

    public Long getId() {
        id++;
        return id;
    }
}

