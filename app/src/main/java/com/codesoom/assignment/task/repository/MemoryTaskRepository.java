package com.codesoom.assignment.task.repository;

import com.codesoom.assignment.task.domain.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryTaskRepository implements TaskRepository {

  private final Map<Long, Task> store = new HashMap<>();
  private long sequence = 0L;

  @Override
  public Task save(Task task) {
    task.setId(++sequence);
    store.put(task.getId(), task);
    return task;
  }

  @Override
  public Optional<Task> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<Task> findAll() {
    return new ArrayList<>(store.values());
  }

  @Override
  public void deleteById(Long id) {
    findById(id).ifPresentOrElse(task -> store.remove(id),
        () -> {
          throw new IllegalArgumentException("Id: " + id + " does not exist in the repository");
        });
  }

  @Override
  public Task updateTitle(Long id, String title) {
    if (!store.containsKey(id)) {
      throw new IllegalArgumentException("Id: " + id + " does not exist in the repository");
    }
    Task task = store.get(id);
    task.setTitle(title);
    store.put(id, task);
    return task;
  }
}
