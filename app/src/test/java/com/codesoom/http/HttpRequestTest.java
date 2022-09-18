package com.codesoom.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpRequestTest {
    @DisplayName("getLongFromPathParameter(idx): HttpRequest의 path에서 지정한 위치(idx)의 값을 Long 타입으로 반환한다")
    @Test
    void getLongFromPathParameter_success() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/tasks/1", "");

        Long id = request.getLongFromPathParameter(2);
        assertThat(id).isEqualTo(1L);
    }

    @DisplayName("getLongFromPathParameter(idx): HttpRequest의 path에서 지정한 위치에(idx) 값이 없을 경우 예외가 발생한다.")
    @Test
    void getLongFromPathParameter_fail_noValue() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/tasks/", "");

        assertThrows(IllegalArgumentException.class, () -> {
                    request.getLongFromPathParameter(2);
                }
        );
    }

    @DisplayName("getLongFromPathParameter(idx): HttpRequest의 path에서 지정한 위치(idx)가 없는 경우 예외가 발생한다.")
    @Test
    void getLongFromPathParameter_fail_noPlace() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/tasks", "");

        assertThrows(IllegalArgumentException.class, () -> {
                    request.getLongFromPathParameter(2);
                }
        );
    }

    @DisplayName("getLongFromPathParameter(idx): HttpRequest의 path에서 지정한 위치(idx)의 값을 Long으로 파싱할 수 없는 경우 예외가 발생한다.")
    @Test
    void getLongFromPathParameter_fail_canNotParseLong() {
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/tasks/aa", "");

        assertThrows(IllegalArgumentException.class, () -> {
                    request.getLongFromPathParameter(2);
                }
        );
    }
}