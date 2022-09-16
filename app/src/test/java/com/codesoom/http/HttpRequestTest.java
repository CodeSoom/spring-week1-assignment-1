package com.codesoom.http;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestTest {
    @DisplayName("정상 -/tasks/1로 요청한 경우")
    @Test
    void getLongFromPathParameterWithCorrect() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/tasks/1", "");

        Long id = request.getLongFromPathParameter(2);
        assertThat(id).isEqualTo(1L);
    }

    @DisplayName("비정상 - /tasks/ 로 요청한 경우 ")
    @Test
    void getLongFromPathParameterWithCorrect2() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/tasks/", "");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    request.getLongFromPathParameter(2);
                }
        );
    }

    @DisplayName("비정상 - /tasks 로 요청한 경우 ")
    @Test
    void getLongFromPathParameterWithCorrect3() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/tasks", "");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    request.getLongFromPathParameter(2);
                }
        );
    }

    @DisplayName("비정상 - /tasks/ss 로 요청한 경우 ")
    @Test
    void getLongFromPathParameterWithCorrect4() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/tasks/aa", "");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    request.getLongFromPathParameter(2);
                }
        );
    }
}