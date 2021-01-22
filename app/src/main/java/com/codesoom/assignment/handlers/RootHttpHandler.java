package com.codesoom.assignment.handlers;

import com.codesoom.assignment.App.ResultMessage;
import com.codesoom.assignment.App.HttpStatusCode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * 루트 도메인 리퀘스트를 처리합니다.
 */

public class RootHttpHandler implements HttpHandler {

    /**
     * 사용자가 Request 를 보내면 실행되는 메서드 입니다.
     * @param exchange Http 통신을 통해 클라이언트에게 전달 받은 데이터가 들어있습니다.
     */

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String requestURIPath = requestURI.getPath();
        String requestMethod = exchange.getRequestMethod();

        String result = ResultMessage.OK.getMessage();
        if(requestURIPath.equals("/") && (requestMethod.equals("GET") || requestMethod.equals("HEAD"))){
            exchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), result.getBytes().length);
        }else{
            result = ResultMessage.NOT_FOUND.getMessage();
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), result.getBytes().length);
        }

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(result.getBytes());
        responseBody.flush();
        exchange.close();
    }
}
