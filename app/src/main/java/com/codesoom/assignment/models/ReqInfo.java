package com.codesoom.assignment.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;

public class ReqInfo {

    private String[] path;
    private String method;
    private String body;

    public ReqInfo(HttpExchange exchange){
        resetPath(exchange);
        resetMethod(exchange);
        resetBody(exchange);
    }

    /**
     * @param exchange
     * @Desc 요청 메서드 초기화
     */
    private void resetMethod(HttpExchange exchange) {
        this.method = exchange.getRequestMethod();
    }

    /**
     * @param exchange
     * @Desc path 값 배열로 초기화
     */
    private void resetPath(HttpExchange exchange){
        String requestPath = exchange.getRequestURI().getPath();
        this.path = requestPath.split("/");
    }

    /**
     * @param exchange
     * @Desc 요청 바디 초기화
     */
    private void resetBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody(); //요청 값을 읽을 수 있는 Stream 반환.
        this.body = new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .collect(Collectors.joining("\n"));
    }

    public String getMethod(){
        return method;
    }

    /**
     * @param n
     * @return n번째 path 값
     * @Desc n번째 path 값을 추출한다.
     */
    public String extractPathValue(int n){
        try{
            return path[n];
        }catch(Exception e){
            return null;
        }

    }

    public String getBody(){
        return body;
    }

}
