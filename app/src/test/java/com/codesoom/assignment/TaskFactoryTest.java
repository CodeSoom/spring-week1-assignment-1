package com.codesoom.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaskFactoryTest {

    @Test
    @DisplayName("task 모델을 생성한다.")
    void toTask() throws JsonProcessingException {
        String content = "{\"title\":\"watch ashal youtube\"}";

        TaskFactory taskFactory = new TaskFactory();
        Task task = taskFactory.toTask(content);

        assertThat(task.getTitle()).isEqualTo("watch ashal youtube");
    }
}
