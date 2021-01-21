package com.codesoom.assignment.service;

public class IdGenerator {
    private static final int START_ID = 0;
    private int idCnt;

    public IdGenerator() {
        this.idCnt = START_ID;
    }

    public long generateNewTaskId() {
        return idCnt++;
    }

}
