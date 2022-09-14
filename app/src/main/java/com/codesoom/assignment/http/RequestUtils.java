package com.codesoom.assignment.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 요청 관련 데이터를 처리하는 유틸 클래스
 */
public class RequestUtils {

    /**
     * 요청 바디를 읽어 반환합니다.
     * @param inputStream
     * @return 요청 바디 문자열
     */
    public static String readRequestBody(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * 요청 메소드에 맞는 요청 데이터를 가지고 있는지 확인합니다. 정상적인 요청이라면 true를 반환합니다.
     * @param requestMethod 요청 메소드
     * @param requestURI 요청 URI
     * @param requestBody 요청 바디
     * @return 정상적인 요청이면 true, 아니면 false
     */
    public static boolean isValidRequest(RequestMethod requestMethod, String requestURI, String requestBody) {
        switch (requestMethod) {
            case GET:
                return "/tasks".equals(requestURI) || checkURIResource(requestURI);
            case POST:
                return "/tasks".equals(requestURI) && !requestBody.isBlank();
            case PUT:
            case PATCH:
                return checkURIResource(requestURI) && !requestBody.isBlank();
            case DELETE:
                return checkURIResource(requestURI);
        }

        return false;
    }

    /**
     * 요청 URI에 아이디가 존재하면 true를 반환합니다.
     * @param requestURI 요청 URI
     * @return 요청 URI에 아이디가 존재하면 true, 아니면 false
     */
    private static boolean checkURIResource(String requestURI) {
        String pattern = "^\\/tasks\\/(\\d+)$";
        return requestURI.matches(pattern);
    }

    /**
     * 요청 URI에 리소스 아이디가 존재하면 아이디를 반환합니다.
     * @param requestURI 요청 URI
     * @return 아이디가 존재하면 아이디, 아니면 빈 Optional 값
     */
    public static Optional<Long> getResourceId(String requestURI) {
        Pattern pattern = Pattern.compile("^\\/tasks\\/(\\d+)$");
        Matcher matcher = pattern.matcher(requestURI);
        if (matcher.find()) {
            return Optional.of(Long.parseLong(matcher.group(1)));
        }
        return Optional.empty();
    }
}
