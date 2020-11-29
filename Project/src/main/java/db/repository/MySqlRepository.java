package db.repository;

import db.connection.ConnectionPool;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class MySqlRepository<T> implements Repository<T> {
    @Override
    public int create(T bean) {
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            CallableStatement cs = connection.prepareCall("CALL create");
        } catch (SQLException e) {
            // TODO:
        }
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
