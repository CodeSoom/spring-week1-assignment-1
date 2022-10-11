package com.codesoom.assignment;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * RootHandler: http 요청에 대한 응답 내용을 만든다
 */
public class ResponseHandler implements HttpHandler  {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Initialize Response Body
        OutputStream responseBody = exchange.getResponseBody();

        try {
            // Write Response Body
            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("response data 한글"+ "\n");
            jsonResponse.append("method= " + exchange.getRequestMethod()+ "\n");

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
