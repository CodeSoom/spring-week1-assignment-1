package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ToDoHttpResponder {

    private HttpExchange exchange;

    public ToDoHttpResponder(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public void sendResponse(int responseCode, String content) throws IOException {
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
