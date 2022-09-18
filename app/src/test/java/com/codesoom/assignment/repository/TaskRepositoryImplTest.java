package com.codesoom.assignment.repository;

import com.codesoom.assignment.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryImplTest {

    private final TaskRepository taskRepository = TaskRepositoryImpl.getInstance();

    @Test
    @DisplayName("멀티스레드환경 동시성 이슈 체크")
    public void 동시에_3만개_요청() throws InterruptedException {
        int threadCount = 30000;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    taskRepository.save(new Task(0, "test"));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertEquals(30000, taskRepository.findAll().size());
    }

}
