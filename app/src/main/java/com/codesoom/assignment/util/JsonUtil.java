package com.codesoom.assignment.util;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Json 관련 Util 메소드를 모아놓은 클래스 입니다.
 *
 * @author DevRunner21
 * @version 1.0
 * @since 2021.05.07
 */
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 주어진 데이터를 Json 형식의 문자열로 변환해 리턴합니다.
     *
     * @param obj Json 형식의 문자열로 변환할 데이터
     * @return Json 형식의 문자열로 변환된 데이터
     * @throws IOException
     */
    public static String toJsonStr(Object obj) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, obj);

        return outputStream.toString();

    }

    /**
     * Json 형식의 문자열을 할 일 객체로 변환하여 반환합니다.
     *
     * @param content
     * @return 변환된 할 일 객체
     * @throws JsonProcessingException
     */
    public static Task jsonStrToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

}
