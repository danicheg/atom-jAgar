package dao;

import entities.leaderboard.Leaderboard;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

interface Dao<T> {
    List<T> getAll();

    List<T> getAllWhere(String... conditions);

    void insert(T insertedEntity);

    void insertAll(T... insertedEntities);

    void update(T updatedEntity);

    void delete(T deleteEntity);

    void deleteAll(T... deleteEntities);
}
