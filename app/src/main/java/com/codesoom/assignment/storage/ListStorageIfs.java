package com.codesoom.assignment.storage;

import java.util.List;

public interface ListStorageIfs<T> {

    T findById(int id);
    T save(T entiry);
    void deleteById(int id);
    List<T> findAll();
}
