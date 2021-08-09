package com.codesoom.assignment.todolist.util;

import java.util.HashMap;
import java.util.Map;

public class PathExtractor {

    public static final String PATH_DELIMITER = "/";

    public static Map<String, Object> extract(String template, String filled) {
        final String[] splintedTemplate = template.split(PATH_DELIMITER);
        final String[] splintedFilled = filled.split(PATH_DELIMITER);

        if (splintedFilled.length != splintedTemplate.length) {
            throw new RuntimeException();
        }

        Map<String, Object> templateMap = new HashMap<>();

        for (int i = 0; i < splintedTemplate.length; i++) {
            final String str = splintedTemplate[i];
            if (str.matches("\\{(.+)}")) {
                templateMap.put(str.substring(1, str.length() - 1), splintedFilled[i]);
            }
        }

        return templateMap;
    }
}
