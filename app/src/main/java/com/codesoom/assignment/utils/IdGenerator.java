package com.codesoom.assignment.utils;

public final class IdGenerator {
    private Long currentId = 1L;

    public Long generateId() {
        final Long id = currentId;
        ++currentId;
        return id;
    }
}
