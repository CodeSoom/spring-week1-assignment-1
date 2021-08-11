package com.codesoom.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaskConvertorTest {

    @Test
    @DisplayName("task 모델로 변환한다.")
    void toTask() throws JsonProcessingException {
        String content = "{\"title\":\"watch ashal youtube\"}";

        TaskConvertor taskConvertor = new TaskConvertor();
        Task task = taskConvertor.toTask(content);

        assertThat(task.getTitle()).isEqualTo("watch ashal youtube");
    }
}