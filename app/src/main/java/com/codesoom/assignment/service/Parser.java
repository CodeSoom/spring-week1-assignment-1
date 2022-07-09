package com.codesoom.assignment.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Parser {
    /**
     * 수신된 Http 요청의 본문을 String으로 변환하여 리턴한다.
     *
     * @param requestBody 수신된 Http 요청의 본문
     * @return 본문을 String으로 변환하여 리턴
     */
    public static String parsingRequest(InputStream requestBody) {
        return new BufferedReader(new InputStreamReader(requestBody))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * 나누어진 path에서 Id에 해당하는 값을 가져와 숫자 형식으로 변환해서 리턴합니다.
     *
     * @param path 특정 문자로 나누어진 경로
     * @return 경로를 숫자로 변환해 리턴
     */
    public static Long extractId(String[] path) {
        return Long.valueOf(path[2]);
    }

    /**
     * Task의 숫자 형식의 id가 경로에 포함되어 있으면 true, 아니면 false를 리턴합니다.
     *
     * @param path 수신된 Http 요청의 경로
     * @return 숫자 형식의 id가 경로에 포함되어 있으면 true, 아니면 false
     */
    public static boolean isDetailMatches(String path) {
        return path.matches("/tasks/[0-9]+");
    }
}
