package com.codesoom.assignment.models;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Path {
    String path;

    public Path(URI uri) {
        this.path = uri.getPath();
    }

    public boolean has(String sub) {
        String regex = "/" + sub + "/?$";
        return Pattern.matches(regex, this.path);
    }

    public boolean hasIdOf(String sub) {
        String regex = "/" + sub + "/[0-9]+/?$";
        return Pattern.matches(regex, this.path);
    }

    public Long getIdOf(String sub) throws NoSuchElementException {
        String regex = "/" + sub + "/([0-9]+)/?$";
        System.out.println(regex);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.path);

        if(!matcher.find()) {
            throw new NoSuchElementException("Not Found id");
        }

        return Long.parseLong(matcher.group(1));
    }
}
