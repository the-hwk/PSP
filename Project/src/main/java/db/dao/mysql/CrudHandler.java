package db.dao.mysql;

import db.connection.ConnectionPool;
import db.dao.CallableStatementParametersSetter;
import db.dao.interfaces.CrudOperations;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CrudHandler<T> implements CrudOperations<T> {
    @Override
    public Optional<T> select(int id) {
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            CallableStatement callState = connection.prepareCall(getCallSelectById());
            getCallStateParamSetterForIdSelect(id).apply(callState);

            ResultSet rs = callState.executeQuery();

            if (rs.next()) {
                return getFields(rs);
            }
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<T> select() {
        List<T> objs = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            CallableStatement callState = connection.prepareCall(getCallSelectAll());

            ResultSet rs = callState.executeQuery();

            while (rs.next()) {
                getFields(rs).ifPresent(objs::add);
            }
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }

        return objs;
    }

    @Override
    public int insert(T obj) {
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            CallableStatement callState = connection.prepareCall(getCallInsert());
            getCallStateParamSetterForInsert(obj).apply(callState);

            ResultSet rs = callState.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
        }

        return 0;
    }

    @Override
    public boolean update(T obj) {
        boolean result = true;

        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            CallableStatement callState = connection.prepareCall(getCallUpdate());
            getCallStateParamSetterForUpdate(obj).apply(callState);

            callState.execute();
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
            result = false;
        }

        return result;
    }

    @Override
    public boolean delete(T obj) {
        boolean result = true;

        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            CallableStatement callState = connection.prepareCall(getCallDelete());
            getCallStateParamSetterForDelete(obj).apply(callState);

            callState.execute();
        } catch (SQLException e) {
            getLogger().error(e.getMessage());
            result = false;
        }

        return result;
    }

    public CallableStatementParametersSetter getCallStateParamSetterForIdSelect(int id) {
        return callState -> callState.setInt(1, id);
    }

    public abstract CallableStatementParametersSetter getCallStateParamSetterForInsert(T obj);

    public abstract CallableStatementParametersSetter getCallStateParamSetterForUpdate(T obj);

    public abstract CallableStatementParametersSetter getCallStateParamSetterForDelete(T obj);

    public abstract Optional<T> getFields(ResultSet rs) throws SQLException;

    public abstract String getCallSelectById();

    public abstract String getCallSelectAll();

    public abstract String getCallInsert();

    public abstract String getCallUpdate();

    public abstract String getCallDelete();

    public abstract Logger getLogger();
}
