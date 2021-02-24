package mff.java.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    /**
     * name of the DB file
     */
    private final static String databaseFileName = "database.db";

    /**
     * get Database connection
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + databaseFileName);
    }
}
