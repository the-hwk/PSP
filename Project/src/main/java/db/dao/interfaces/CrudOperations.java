package db.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudOperations<T> {
    Optional<T> select(int id);
    List<T> select();
    int insert(T obj);
    boolean update(T obj);
    boolean delete(T obj);
}
