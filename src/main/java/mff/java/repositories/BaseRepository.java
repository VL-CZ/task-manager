package mff.java.repositories;

import mff.java.db.DbManager;

public abstract class BaseRepository<T> implements IRepository<T> {

    /**
     * connection to the database
     */
    protected final DbManager dbManager;

    protected BaseRepository(DbManager dbManager) {
        this.dbManager = dbManager;
    }
}
