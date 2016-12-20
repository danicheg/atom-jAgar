package dao;

import java.util.List;

interface Dao<T> {
    List<T> getAll();
    List<T> getAllWhere(String... conditions);
    void insert(T insertedEntity);
    void insertAll(T... insertedEntities);
    void update(T updatedEntity);
    void delete(T deleteEntity);
    void deleteAll(T... deleteEntities);
}
