package com.codesoom.assignment.modles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class Task {
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
