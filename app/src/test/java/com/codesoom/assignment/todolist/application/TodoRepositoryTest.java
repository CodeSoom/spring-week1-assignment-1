package com.codesoom.assignment.todolist.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TodoList Repository 테스트")
class TodoRepositoryTest {
    private TodoRepository repository;

    @BeforeEach
    public void init() {
        repository = TodoRepository.getInstance();
    }
}
