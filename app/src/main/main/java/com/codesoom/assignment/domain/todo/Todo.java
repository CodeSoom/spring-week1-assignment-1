package com.codesoom.assignment.domain.todo;


import com.codesoom.assignment.domain.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

public class Todo {

    private String todoId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Todo(String title) {
        this.todoId = generateTodoId();
        this.title = title;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private String generateTodoId() {
        return UUID.randomUUID()
                    .toString()
                    .replaceAll("-", "")
                    .substring(0, 8);
    }

    public Todo updateTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getTodoId() {
        return todoId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public static class TodoBuilder implements Builder<Todo> {
        private String title;
        public TodoBuilder(){

        }
        public TodoBuilder title(String title) {
            this.title = title;
            return this;
        }

        @Override
        public Todo builder() {
            return new Todo(title);
        }
    }
}
