package com.codesoom.assignment.modles;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class Task {
    public static final String TO_JSON_FAIL_ERROR = "Json conversion fail.";

    private Long id;
    private String titie;

    public Task(final Long id, final String titie) {
        this.id = id;
        this.titie = titie;
    }

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

    public String toJsonOrNull() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final OutputStream outputStream = new ByteArrayOutputStream();
        try {
            objectMapper.writeValue(outputStream, this);
            return outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
