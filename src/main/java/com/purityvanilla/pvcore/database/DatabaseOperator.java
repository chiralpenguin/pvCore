package com.purityvanilla.pvcore.database;

public abstract class DatabaseOperator {
    protected final DatabaseConnector database;

    public DatabaseOperator(DatabaseConnector database) {
        this.database = database;

        createTables();
    }

    protected void createTables() {

    }
}
