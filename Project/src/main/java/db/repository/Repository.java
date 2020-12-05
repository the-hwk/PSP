package db.repository;

import java.util.List;

public interface Repository<T> {
    T findById(int id);
    List<T> findAll();
    void save(T obj);
    void update(T obj);
    void delete(T obj);
}
