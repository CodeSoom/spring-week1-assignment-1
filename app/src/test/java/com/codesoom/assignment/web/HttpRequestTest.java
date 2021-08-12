package com.codesoom.assignment.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestTest {

    @Test
    @DisplayName("허용되지 않은 메서드를 전달하면 false를 반환")
    void isAllowedMethod() {
        HttpRequest httpRequest = new HttpRequest("/tasks", "TRUNC");

        boolean actual = httpRequest.isAllowedMethod();

        assertThat(actual).isFalse();
    }
}
