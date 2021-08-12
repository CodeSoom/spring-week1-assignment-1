package com.codesoom.assignment;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppTest {
    @Test
    void appHasAGreeting() throws URISyntaxException {
        App classUnderTest = new App();
        URI uri = new URI("http://www.naver.com/tasks/12/32");
        Path path = Paths.get(uri.getPath());
        System.out.println(path);
        System.out.println(path.getParent());
        System.out.println(path.getName(0));
        String a = path.getName(0).toString();

        /*System.out.println(path.getName(0));
        System.out.println(path.getName(1));*/
        System.out.println(path.getNameCount());
    }
}
