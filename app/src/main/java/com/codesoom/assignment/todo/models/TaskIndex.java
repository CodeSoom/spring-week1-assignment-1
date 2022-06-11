package com.codesoom.assignment.todo.models;

public class TaskIndex {
  private static Long lastIndex;

  public TaskIndex() {
    lastIndex = 0L;
  }

  public Long getNextIndex() {
    return lastIndex + 1;
  }

  public synchronized void addLastIndex() {
    lastIndex += lastIndex + 1;
  }
}
