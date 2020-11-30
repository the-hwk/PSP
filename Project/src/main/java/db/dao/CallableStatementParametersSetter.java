package db.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface CallableStatementParametersSetter {
    void apply(CallableStatement callState) throws SQLException;
}
