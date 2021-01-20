package com.codesoom.assignment.models;

import com.codesoom.assignment.errors.AlreadyExistsIDException;
import com.codesoom.assignment.errors.NotExistsIDException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The class {@code TaskManager} is managing tasks.
 * @see Task
 */
public class TaskManager {
    private static long index = 0;
    private static final HashMap<Long, Task> TASKS = new HashMap<>();

    /**
     * Find all tasks.
     *
     * @return all tasks.
     */
    public static List<Task> find() {
        return new ArrayList<>(TASKS.values());
    }

    /**
     * Find one task.
     *
     * @param id target id.
     * @return target task.
     * @throws NotExistsIDException if id is not exist.
     */
    public static Task find(long id) throws NotExistsIDException {
        Task task = TASKS.get(id);

        if (task == null) {
            throw new NotExistsIDException();
        }
        return task;
    }

    /**
     * Insert received task.
     *
     * @param task want to insert.
     * @throws AlreadyExistsIDException if id is already exist.
     */
    public static void insert(Task task) throws AlreadyExistsIDException {
        if (isExist(task.id())) {
            throw new AlreadyExistsIDException();
        }
        TASKS.put(task.id(), task);
    }

    /**
     * Insert task.
     * id insert to auto increment value.
     *
     * @param title task title.
     * @return inserted task.
     */
    public static Task insert(String title) {
        index += 1;
        if (!isExist(index)) {
            Task task = new Task(index, title);
            TASKS.put(index, task);
            return task;
        }
        return insert(title);
    }

    /**
     * Modify task.
     *
     * @param task want to modify task object.
     * @throws NotExistsIDException if id is not exist.
     */
    public static void modify(Task task) throws NotExistsIDException {
        if (!isExist(task.id())) {
            throw new NotExistsIDException();
        }
        TASKS.replace(task.id(), task);
    }

    /**
     * Modify task.
     *
     * @param id target id
     * @param title want to change content.
     * @return modified task.
     * @throws NotExistsIDException if id is not exist.
     */
    public static Task modify(long id, String title) throws NotExistsIDException {
        if (!isExist(id)) {
            throw new NotExistsIDException();
        }
        Task task = new Task(id, title);

        TASKS.replace(task.id(), task);
        return task;
    }

    /**
     * Delete task.
     *
     * @param id target id.
     * @throws NotExistsIDException if id is not exist.
     */
    public static void delete(long id) throws NotExistsIDException {
        if (!isExist(id)) {
            throw new NotExistsIDException();
        }
        TASKS.remove(id);
    }

    /**
     * Remove all tasks and initialization {@code index}
     */
    public static void clear() {
        TASKS.clear();
        index = 0;
    }

    /**
     * Check id is exist.
     *
     * @param id target id.
     * @return true if id is exists.
     */
    private static boolean isExist(long id) {
        return TASKS.get(id) != null;
    }
}
