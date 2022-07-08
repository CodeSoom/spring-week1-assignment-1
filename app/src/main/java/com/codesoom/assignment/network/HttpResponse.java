package com.codesoom.assignment.network;

import com.sun.net.httpserver.HttpExchange;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Http 응답을 전달하는 객체
 */
public class HttpResponse {

    private final HttpExchange exchange;
    private static final int EMPTY_RESPONSE_BYTE_LENGTH = -1;

    /**
     * Http Response 전송을 위한 객체를 생성합니다
     * @param exchange HttpExchange
     */
    public HttpResponse(HttpExchange exchange) {
        this.exchange = exchange;
    }

    /**
     * @return ResponseCode
     */
    public int getResponseCode() {
        return exchange.getResponseCode();
    }

    /**
     * @return ResponseBody
     */
    public OutputStream getResponseBody() {
        return exchange.getResponseBody();
    }

    /**
     * 응답을 전송합니다
     * @param responseCode 응답코드
     * @param content 응답에 담을 content
     * @throws IOException outputStream 쓰기 작업에서 발생하는 에러 전달
     */
    public void send(HttpResponseCode responseCode, @Nullable String content) throws IOException {
        if (content == null) {
            exchange.sendResponseHeaders(responseCode.getRawValue(), EMPTY_RESPONSE_BYTE_LENGTH);
        } else {
            exchange.sendResponseHeaders(responseCode.getRawValue(), content.getBytes().length);

            try (OutputStream outputStream = exchange.getResponseBody()) { // try-with-resources try 구문이 종료되면 자동으로 stream에 관련된 리소스들을 해제해준다. (AutoCloseable Interface)
                outputStream.write(content.getBytes()); // 전달받은 byte array로부터 output stream에 기록하기
                outputStream.flush(); // 버퍼에 담겨있는 output byte들을 강제로 기록되게 한다. 버퍼에 효율적으로 담아두는 것을 추측해볼 수 있음. (Flushable Interface)
            } catch (IOException e) {
                throw e;
            }
        }
    }
}
