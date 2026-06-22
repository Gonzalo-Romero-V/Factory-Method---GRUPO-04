package ec.espoch.biblioteca.db;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String PROD_URL = "jdbc:h2:./data/biblioteca";
    private static final String USER = "sa";
    private static final String PASS = "";

    private static DatabaseManager instance;
    private final Connection connection;

    private DatabaseManager(String url) {
        try {
            Flyway.configure()
                    .dataSource(url, USER, PASS)
                    .locations("classpath:db/migration")
                    .load()
                    .migrate();
            connection = DriverManager.getConnection(url, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error inicializando base de datos: " + e.getMessage(), e);
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager(PROD_URL);
        }
        return instance;
    }

    /** Instancia aislada para tests — usa H2 en memoria. */
    public static DatabaseManager forTesting() {
        String url = "jdbc:h2:mem:test_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1";
        return new DatabaseManager(url);
    }

    public Connection getConnection() {
        return connection;
    }
}
