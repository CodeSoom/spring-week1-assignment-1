package com.codesoom.assignment.network;

import com.sun.net.httpserver.HttpExchange;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Http 응답을 전달하는 객체
 */
public class HttpResponse {

    private HttpExchange exchange;

    public HttpResponse(HttpExchange exchange) {
        this.exchange = exchange;
    }

    /**
     * 응답을 전송합니다
     * @param responseCode 응답코드
     * @param content 응답에 담을 content
     * @throws IOException
     */
    public void send(int responseCode, @Nullable String content) throws IOException {
        if (content == null) {
            exchange.sendResponseHeaders(responseCode, -1);
        } else {
            exchange.sendResponseHeaders(responseCode, content.getBytes().length);

            final OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(content.getBytes()); // 전달받은 byte array로부터 output stream에 기록하기
            outputStream.flush(); // 버퍼에 담겨있는 output byte들을 강제로 기록되게 한다. 버퍼에 효율적으로 담아두는 것을 추측해볼 수 있음. (Flushable Interface)
            outputStream.close(); // 이 stream에 관련된 리소스들을 해제해준다. (Closeable Interface)
        }
    }
}
