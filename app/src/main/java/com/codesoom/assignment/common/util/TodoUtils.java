package com.codesoom.assignment.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TodoUtils {

    public static Map<String,Object> transferStringToJson(String string){
        ObjectMapper objectMapper =new ObjectMapper();
        Map<String,Object> transferStringToMap=new HashMap<>();
        try {
            transferStringToMap=objectMapper.readValue(string,Map.class);
        }catch (IOException e){
            System.out.printf(" transferStringToJson -> {}",e.getMessage());
        }
        return transferStringToMap;
    }

    public static String transferJsonToString(Object json){
        ObjectMapper objectMapper =new ObjectMapper();
        String transferJsonToString = null;
        try {
            transferJsonToString=objectMapper.writeValueAsString(json);
        }catch (IOException e){
            System.out.printf(" transferJsonToString -> {}",e.getMessage());
        }
        return transferJsonToString;
    }
}
