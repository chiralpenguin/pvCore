package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.pvCore;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class DatabaseHandler {
    private final pvCore plugin;
    private final HikariDataSource dataSource;
    private final PlayerDataService playerData;

    public DatabaseHandler(pvCore plugin) {
        this.plugin = plugin;
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
        playerData = new PlayerDataService(plugin);
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            Component warningMessage = plugin.config().getMessage("database-connection-failure");
            plugin.getLogger().warning(PlainTextComponentSerializer.plainText().serialize(warningMessage));
            plugin.getLogger().warning(getStackTrace(e));
            return null;
        }
    }

    public void logQueryException(pvCore plugin, String query, SQLException e) {
        String rawMessage = plugin.config().getRawMessage("database-query-failure");
        TagResolver resolver = TagResolver.resolver(Placeholder.component("query", Component.text(query)));
        Component warningMessage = MiniMessage.miniMessage().deserialize(rawMessage, resolver);
        plugin.getLogger().warning(PlainTextComponentSerializer.plainText().serialize(warningMessage));
        plugin.getLogger().warning(getStackTrace(e));
    }

    public PreparedStatement prepareStatement(String query, List<String> params) {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);

            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }

            return pstmt;
        } catch (SQLException e) {
            logQueryException(plugin, query, e);
            return null;
        }
    }

    // Generics used to allow anonymous functions to process ResultSet and capture any exceptions
    public <T> T executeQuery(String query, List<String> params, ResultSetProcessor<T> processor) {
        PreparedStatement pstmt = prepareStatement(query, params);
        try {
            ResultSet rs = pstmt.executeQuery();
            return processor.process(rs);
        } catch (SQLException e) {
            logQueryException(plugin, query, e);
            return null;
        }
    }

    public <T> T executeQuery(String query, ResultSetProcessor<T> processor) {
        return executeQuery(query, new ArrayList<>(), processor);
    }

    public int executeUpdate(String query, List<String> params) {
        PreparedStatement pstmt = prepareStatement(query, params);
        try {
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            logQueryException(plugin, query, e);
            return 0;
        }
    }

    public int executeUpdate(String query) {
        return executeUpdate(query, new ArrayList<>());
    }


}
