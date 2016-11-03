package dao;

import java.util.List;
import java.util.Optional;

/**
 * Created by s.rybalkin on 17.10.2016.
 */
public interface Dao<T> {
    List<T> getAll();
}
