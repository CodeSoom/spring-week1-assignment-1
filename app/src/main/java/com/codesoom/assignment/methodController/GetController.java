package com.codesoom.assignment.methodController;

import java.io.IOException;
import java.io.OutputStream;

import com.codesoom.assignment.methodController.util.UtfEncoder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class GetController {

    // get 요청을 처리한다
    public void handler(HttpExchange exchange, OutputStream responseBody) throws IOException {
        try {
            // Write Response Body
            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("response data 한글" + "\n");
            jsonResponse.append("GET 요청을 처리한다");

            // Encoding to UTF-8
            UtfEncoder encoder = new UtfEncoder(jsonResponse);

            // Set Response Headers
            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "text/html;charset=UTF-8");
            headers.add("Content-Length", String.valueOf(encoder.getContentLength()));

            // Send Response Headers
            exchange.sendResponseHeaders(200, encoder.getContentLength());

            responseBody.write(encoder.getContent());

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
