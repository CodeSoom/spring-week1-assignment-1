package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import javax.swing.text.html.Option;

public class JsonMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    public User toUser(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, User.class);
    }
}
