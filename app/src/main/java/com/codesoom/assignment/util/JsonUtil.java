package com.codesoom.assignment.util;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JsonString으로 변환한다.
     * @param obj
     * @return
     * @throws IOException
     */
    public static String toJsonStr(Object obj) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, obj);

        return outputStream.toString();

    }

    /**
     * JsonString을 Task 객체로 변환한다.
     * @param content
     * @return
     * @throws JsonProcessingException
     */
    public static Task jsonStrToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

}
