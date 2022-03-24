package com.codesoom.assignment.common.util;

import com.codesoom.assignment.common.logger.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TodoUtils {
    private static final Logger logger = Log.getInstance().getLog(TodoUtils.class);

    public static Map<String, Object> transferStringToJson(String string) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> transferStringToMap = new HashMap<String, Object>();
        try {
            transferStringToMap = objectMapper.readValue(string, Map.class);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        return transferStringToMap;
    }

    public static String transferJsonToString(Object json) {
        ObjectMapper objectMapper = new ObjectMapper();
        String transferJsonToString = null;
        try {
            transferJsonToString = objectMapper.writeValueAsString(json);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        return transferJsonToString;
    }

    public static byte[] convertToBytes(Object object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }
}
