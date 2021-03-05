package mff.java.db;

import mff.java.utils.PathUtils;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {
    /**
     * name of the DB file
     */
    private static final String databaseFileName = "database.db";

    /**
     * name of the SQL script for creating tables
     */
    private static final String createSqlScriptFileName = "createTables.sql";

    /**
     * get Database connection
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        SQLiteConfig dbConfig = new SQLiteConfig();
        dbConfig.enforceForeignKeys(true);
        return DriverManager.getConnection("jdbc:sqlite:" + databaseFileName, dbConfig.toProperties());
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
        catch (SQLException | IOException | URISyntaxException exception) {
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
     * @throws URISyntaxException
     * @throws IOException
     */
    private void tryToCreateTables() throws SQLException, URISyntaxException, IOException {
        String createTablesSql = Files.readString(getSqlScriptPath());

        try (var connection = getConnection()) {
            var statement = connection.createStatement();
            statement.execute(createTablesSql);
        }
    }

    /**
     * get path of the SQL create tables script
     *
     * @return path to SQL create tables script
     */
    private Path getSqlScriptPath() {
        return Paths.get(PathUtils.getResourcesFolderPath().toString(), "mff", "java", createSqlScriptFileName);
    }
}
