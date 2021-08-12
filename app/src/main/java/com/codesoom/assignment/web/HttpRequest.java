package com.codesoom.assignment.web;

import com.codesoom.assignment.errors.MethodNotAllowedException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HttpRequest {

    public static final String PREFIX_PATH = "/tasks";

    private final String path;
    private final AllowMethods method;

    public HttpRequest(String path, String method) {
        this.path = path;
        this.method = AllowMethods.fromString(method)
            .orElseThrow(MethodNotAllowedException::new);
    }

    public Long getTaskIdFromPath() {
        String replaced = path.replace(PREFIX_PATH, "")
            .replace("/", "");

        return Long.parseLong(replaced);
    }

    public boolean isReadAll() {
        return AllowMethods.GET.equals(method) && PREFIX_PATH.equals(path);
    }

    public boolean isReadOne() {
        return AllowMethods.GET.equals(method) && hasTaskId();
    }

    public boolean isCreateOne() {
        return AllowMethods.POST.equals(method) && PREFIX_PATH.equals(path);
    }

    public boolean isUpdateOne() {
        return (AllowMethods.PUT.equals(method) || AllowMethods.PATCH.equals(method))
            && hasTaskId();
    }

    public boolean isDeleteOne() {
        return AllowMethods.DELETE.equals(method) && hasTaskId();
    }

    private boolean hasTaskId() {
        return Pattern.matches("/tasks/[0-9]+$", path);
    }

    @Override
    public String toString() {
        return String.format("HttpRequest {method=%s, path=%s} ", method, path);
    }

    private enum AllowMethods {
        GET, POST, PUT, PATCH, DELETE;

        private static final Map<String, AllowMethods> stringToEnum = Arrays.stream(values())
            .collect(
                Collectors.toMap(Enum::toString, method -> method));

        public static Optional<AllowMethods> fromString(String method) {
            return Optional.ofNullable(stringToEnum.get(method));
        }
    }
}
