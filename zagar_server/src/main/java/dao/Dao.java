package dao;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();
    void insert(T insertedEntity);
}
