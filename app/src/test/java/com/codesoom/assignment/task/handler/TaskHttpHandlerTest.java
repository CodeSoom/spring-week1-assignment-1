package com.codesoom.assignment.task.handler;

import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.handler.TaskHttpHandler;
import com.codesoom.assignment.task.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskHttpHandlerTest {

    @Test
    @DisplayName("/tasks/{id} 파싱 테스트")
    void pasingPath() {

    }
}