package com.codesoom.assignment.storage;

import java.util.ArrayList;
import java.util.List;

public class ListStorageRepositoryAbstract<T extends ListStorageEntity> implements ListStorageIfs<T> {

    protected final List<T> list = new ArrayList<>();
    private long newId = 0L;


    @Override
    public T findById(long id) {
        return list.stream().filter(it -> it.getId() == id).findFirst().orElse(null);
    }

    @Override
    public T save(T entity) {
        var optionalEntity = list.stream().filter(it -> it.getId() == entity.getId()).findFirst();

        if (optionalEntity.isEmpty()) {
            entity.setId(generateId());
        } else {
            delete(optionalEntity.get());
        }

        list.add(entity);

        return entity;
    }

    @Override
    public void deleteById(long id) {
        var optionalEntity = list.stream().filter(it -> it.getId() == id).findFirst();

        if (optionalEntity.isPresent()) {
            list.remove(optionalEntity.get());
        }
    }

    @Override
    public void delete(T entity) {
        list.remove(entity);
    }

    @Override
    public List<T> findAll() {
        return list;
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }

    public long lastId() {
        return newId;
    }
}
