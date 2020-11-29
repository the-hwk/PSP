package db.repository;

import java.util.List;

class MySqlRepository<T> implements Repository<T> {
    @Override
    public int create(T bean) {
        return 0;
    }

    @Override
    public T read(RepositoryParam param) {
        return null;
    }

    @Override
    public List<T> read() {
        return null;
    }

    @Override
    public boolean update(T bean) {
        return false;
    }

    @Override
    public boolean delete(T bean) {
        return false;
    }

    @Override
    public List<T> query(RepositoryParam param) {
        return null;
    }
}
