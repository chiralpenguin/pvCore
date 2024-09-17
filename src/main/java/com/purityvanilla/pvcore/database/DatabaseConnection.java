package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnection {
    private pvCore plugin;
    private HikariDataSource dataSource;

    public DatabaseConnection(pvCore plugin) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(plugin.config().getJdbcUrl());
        config.setUsername(plugin.config().getDbUser());
        config.setPassword(plugin.config().getDbPassword());
        config.setMaximumPoolSize(plugin.config().getHikariMaxSize());
        config.setMinimumIdle(plugin.config().getHikariMinIdle());
        config.setIdleTimeout(plugin.config().getHikariTimeout());
        config.setMaxLifetime(plugin.config().getHikariLifetime());
        config.setDriverClassName("org.mariadb.jdbc.Driver");

        dataSource = new HikariDataSource(config);
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
