package com.codesoom.assignment.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.stream.Collectors;

/**
 * 1. 클래스명 수정 ReqInfo > RequestInfo
 *  - 아주 명확한게 아니라면 클래스명을 줄여쓰는 것은 지양. 단, 언어에 따라 관습적으로 클래스 명을 줄여쓰는 경우도 있음.
 */
public class RequestInfo {

    private String[] pathSegments;
    private String method;
    private String body;

    private static int MAX_PATH_SEGMENTS_LENGTH = 3;

    public RequestInfo(HttpExchange exchange) throws NumberFormatException{
        resetPathSegments(exchange);
        validationPathSegments(exchange);
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
    private void resetPathSegments(HttpExchange exchange){
        String requestPath = exchange.getRequestURI().getPath();
        this.pathSegments = requestPath.split("/");

    }

    /**
     * @throws NumberFormatException
     * @desc pathSegments에 대한 validation 체크
     *  1. 두번째 세크먼트에 값(ID)이 숫자인가
     */
    private void validationPathSegments(HttpExchange exchange) throws NumberFormatException {

        if(existId()){
            Long.parseLong(pathSegments[2]);
        }
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


    /**
     * @Desc path segments 중 id 값에 해당하는 값이 존재하는지 체크한다.
     */
    public boolean existId(){

        if(pathSegments.length == MAX_PATH_SEGMENTS_LENGTH){
            return true;
        }
        return false;
    }

    /**
     * @Desc path segments 중 id에 해당하는 값을 추출한다.
     */
    public Long extractId(){

        return Long.parseLong(pathSegments[2]);

    }

    /**
     * @Desc path segments 중 id에 해당하는 값을 추출한다.
     * @Feedback
     *  1. 함수를 사용하는 입장에서는 이 함수가 null을 반환하거나, null 체크를 해야한다는 것을 알아차릴 수 없음.
     */
    public String extractFirstPathSegment(){

        return pathSegments[1];

    }

    public String getBody(){ return body; }

    public String getMethod(){
        return method;
    }

}
