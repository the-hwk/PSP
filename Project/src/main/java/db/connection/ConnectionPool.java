package db.connection;

import tools.ResourceManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private static ConnectionPool instance;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections;

    public static ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            instance = init();
        }
        return instance;
    }

    private static ConnectionPool init() throws SQLException {
        ResourceManager manager = new ResourceManager("connection");
        String url = manager.getProperty("url");
        String user = manager.getProperty("user");
        String password = manager.getProperty("password");
        int poolSize = Integer.parseInt(manager.getProperty("poolSize"));
        return new ConnectionPool(url, user, password, poolSize);
    }

    private ConnectionPool(String url, String user, String password, int poolSize) throws SQLException {
        connectionPool = new ArrayList<>(poolSize);
        usedConnections = new ArrayList<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            connectionPool.add(createConnection(url, user, password));
        }
    }

    public synchronized Connection getConnection() {
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    private static Connection createConnection(
            String url, String user, String password)
            throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}