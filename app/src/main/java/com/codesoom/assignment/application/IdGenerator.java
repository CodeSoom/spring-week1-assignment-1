package com.codesoom.assignment.application;

public class IdGenerator {
    private static final int START_ID = 0;
    private int idCounter;

    public IdGenerator() {
        this.idCounter = START_ID;
    }

    public long generateNewTaskId() {
        return idCounter++;
    }
}
