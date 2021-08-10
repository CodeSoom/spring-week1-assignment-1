package com.codesoom.assignment.modles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class Task {
    public static final String TO_JSON_FAIL = "Json conversion fail.";
    public static final String TO_TASK_FAIL = "Task conversion fail.";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Long id;
    private String titie;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitie() {
        return this.titie;
    }

    public void setTitie(String titie) {
        this.titie = titie;
    }
}
