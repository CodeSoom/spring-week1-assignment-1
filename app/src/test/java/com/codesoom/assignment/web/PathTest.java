package com.codesoom.assignment.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathTest {

    @Test
    @DisplayName("taskId 얻기")
    void getTaskId() {
        Path path = new Path("/tasks/11");

        assertThat(path.getTaskId()).isEqualTo("11");
    }

    @Test
    @DisplayName("경로에 taskId가 있는지 확인")
    void hasTaskId() {
        Path path = new Path("/tasks");
        assertThat(path.hasTaskId()).isFalse();

        path = new Path("/tasks/11");
        assertThat(path.hasTaskId()).isTrue();
    }

    @Test
    @DisplayName("/tasks 경로인지 확인")
    void isTasksPath() {
        Path path = new Path("/tasks");

        boolean actual = path.isTasksPath();

        assertThat(actual).isTrue();
    }
}
