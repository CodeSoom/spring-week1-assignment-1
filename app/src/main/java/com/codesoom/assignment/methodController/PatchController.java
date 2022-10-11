package com.codesoom.assignment.methodController;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class PatchController {

    // patch 요청을 처리한다
    public void handler(HttpExchange exchange, OutputStream responseBody) throws IOException {
        try {
            // Write Response Body
            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("response data 한글" + "\n");
            jsonResponse.append("PATCH 요청을 처리한다");

            // Encoding to UTF-8
            ByteBuffer byteData = Charset.forName("UTF-8").encode(jsonResponse.toString());
            int contentLength = byteData.limit();
            byte[] content = new byte[contentLength];
            byteData.get(content, 0, contentLength);

            // Set Response Headers
            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "text/html;charset=UTF-8");
            headers.add("Content-Length", String.valueOf(contentLength));

            // Send Response Headers
            exchange.sendResponseHeaders(200, contentLength);

            responseBody.write(content);

            responseBody.close();

        } catch (IOException e) {
            e.printStackTrace();

            if (responseBody != null) {
                responseBody.close();
            }
        } finally {
            exchange.close();
        }
    }
}
