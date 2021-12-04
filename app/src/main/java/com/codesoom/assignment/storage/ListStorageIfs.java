package com.codesoom.assignment.storage;

import java.util.List;

public interface ListStorageIfs<T> {

    T findById(long id);
    T save(T entity);
    void deleteById(long id);
    void delete(T entity);
    List<T> findAll();
}
