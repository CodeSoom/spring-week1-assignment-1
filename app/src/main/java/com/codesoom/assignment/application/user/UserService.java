package com.codesoom.assignment.application.user;

import com.codesoom.assignment.application.IdGenerator;
import com.codesoom.assignment.domain.User;

import java.util.*;

public class UserService {
    private Map<Long, User> users = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    public User getUser(long id) {
        return getUserById(id);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User createNewUsers(String title, int age) {
        User user = new User(idGenerator.generateNewTaskId(), title, age);
        users.put(user.getId(), user);
        System.out.println("Completed to create user - " + user.toString());
        return user;
    }

    public User updateUser(long id, String name, int age) {
        User user = getUserById(id);
        user.setName(name);
        user.setAge(age);
        System.out.println("Completed to update user - " + user.toString());
        return user;
    }

    public void deleteUser(long id) {
        User user = getUserById(id);
        users.remove(user.getId());
        System.out.println("Completed to delete user - " + user.toString());
    }


    private User getUserById(long id) throws IllegalArgumentException {
        return findUserById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find user (ID: " + id + ")"));
    }

    private Optional<User> findUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }
}
