package com.codesoom.assignment.service;

import com.codesoom.assignment.application.user.UserService;
import com.codesoom.assignment.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
  UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService();
  }

  @Test
  public void testGetUserFail() {
    assertThrows(IllegalArgumentException.class, () -> userService.getUser(0));
  }

  @Test
  public void testUpdateFail() {
    assertThrows(IllegalArgumentException.class, () -> userService.updateUser(0, "Kim", 10));
  }

  @Test
  public void testDeleteFail() {
    assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(0));
  }

  @Test
  public void testGetEmptyUserList() {
    List<User> users = userService.getUsers();
    assertNotNull(users);
    assertTrue(userService.getUsers().isEmpty());
  }

  @Test
  public void testCreateUser() {
    String name = "Kim";
    int age = 10;
    User newUser = createUser(name, age);

    assertNotNull(newUser);
    assertEquals(name, newUser.getName());
    assertEquals(age, newUser.getAge());
  }

  @Test
  public void testUpdateUser() {
    String name = "Kim";
    int age = 10;
    User newUser = createUser(name, age);

    assertNotNull(newUser);
    assertEquals(name, newUser.getName());
    assertEquals(age, newUser.getAge());

    String newName = "Lee";
    int newAge = 15;
    User updatedUser = userService.updateUser(newUser.getId(), newName, newAge);

    assertNotNull(updatedUser);
    assertEquals(newName, updatedUser.getName());
    assertEquals(newAge, updatedUser.getAge());
  }

  @Test
  public void testDeleteUser() {
    String name = "Kim";
    int age = 10;
    User newUser = createUser(name, age);

    assertNotNull(newUser);
    assertEquals(name, newUser.getName());
    assertEquals(age, newUser.getAge());
    assertEquals(1, userService.getUsers().size());

    userService.deleteUser(newUser.getId());
    assertTrue(userService.getUsers().isEmpty());
  }

  User createUser(String name, int age) {
    return userService.createNewUsers(name, age);
  }
}
