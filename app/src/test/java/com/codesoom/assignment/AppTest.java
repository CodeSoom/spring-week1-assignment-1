package com.codesoom.assignment;

import com.google.common.base.Splitter;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppTest {
    @Test
    void appHasAGreeting() throws URISyntaxException {
        App classUnderTest = new App();

        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(1);
        task.setTitle("111");
        tasks.add(task);

        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("111");
        tasks.add(task2);

        tasks.stream().forEach(s -> System.out.println(s.getId()));

        Optional<Task> stream = tasks.stream().filter(obj -> !"12".equals(obj.getId().toString())).findFirst();
        System.out.println(stream.get().getId());
        System.out.println(stream.get().getTitle());


    }
}
