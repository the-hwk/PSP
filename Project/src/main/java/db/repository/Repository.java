package db.repository;

import java.util.List;

public interface Repository<T> {
    int create(T bean);
    T read(RepositoryParam param);
    List<T> read();
    boolean update(T bean);
    boolean delete(T bean);
    List<T> query(RepositoryParam param);
}
