package com.codesoom.assignment.util;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonObjectMapper {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String toJsonArray(Object object) throws IOException {
		OutputStream outputStream = new ByteArrayOutputStream();
		objectMapper.writeValue(outputStream, object);
		return outputStream.toString();
	}

	public static String toJson(Object object) throws IOException {
		return objectMapper.writeValueAsString(object);
	}

	public static <T> T toObject(String jsonString, Class<T> valueType) throws JsonProcessingException {
		return objectMapper.readValue(jsonString, valueType);
	}
}
