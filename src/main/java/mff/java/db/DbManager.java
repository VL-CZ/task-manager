package mff.java.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {
    /**
     * name of the DB file
     */
    private final String databaseFileName = "database.db";

    /**
     * get Database connection
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + databaseFileName);
    }

    /**
     * initialize the DB and tables
     * creates the DB file and tables (if not exist)
     */
    public void initialize() {
        try {
            tryToCreateDb();
            tryToCreateTables();
        }
        catch (SQLException | IOException exception) {
            throw new RuntimeException("Error while initializing the database.");
        }
    }

    /**
     * creates the database file, if not present
     * if it already exists, then it doesn't do anything
     *
     * @throws IOException
     */
    private void tryToCreateDb() throws IOException {
        var database = new File(databaseFileName);
        database.createNewFile();
    }

    /**
     * creates the task table in the database if not exists
     * if it already exists, then it doesn't do anything
     *
     * @throws SQLException
     */
    private void tryToCreateTables() throws SQLException {
        String createTasksTableSql = """
                CREATE TABLE IF NOT EXISTS "tasks" (
                    "id"	INTEGER NOT NULL UNIQUE,
                    "title"	TEXT,
                    "description"	TEXT,
                    "status"	INTEGER,
                    "estimation"	INTEGER,
                    PRIMARY KEY("id" AUTOINCREMENT)
                )
                """;

        try (var connection = getConnection()) {
            var statement = connection.createStatement();
            statement.execute(createTasksTableSql);
        }
    }
}
